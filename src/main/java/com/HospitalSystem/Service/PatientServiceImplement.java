package com.HospitalSystem.Service;

import com.HospitalSystem.Entity.*;
import com.HospitalSystem.Map.DoctorArrangementMap;
import com.HospitalSystem.Map.RegistrationMap;
import com.HospitalSystem.Mapper.DepartmentMapper;
import com.HospitalSystem.Mapper.DoctorArrangementMapper;
import com.HospitalSystem.Mapper.PatientMapper;
import com.HospitalSystem.Mapper.RegistrationMapper;
import com.HospitalSystem.Utils.DateJSON;
import com.HospitalSystem.Utils.JWTUtils;
import com.HospitalSystem.Utils.PatientArrangementResponse;
import com.HospitalSystem.Utils.PatientRecordsResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service("patientService")
public class PatientServiceImplement implements PatientService {
    @Autowired
    private PatientMapper patientMapper;

    @Autowired
    private RegistrationMapper registrationMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private DoctorArrangementMapper doctorArrangementMapper;

    @Override
    public Map<String, Object> getServerTime() {
        LocalDateTime current = LocalDateTime.now();
        String dayOfWeek;

        switch (current.getDayOfWeek()) {
            case MONDAY:
                dayOfWeek = "周一";
                break;
            case TUESDAY:
                dayOfWeek = "周二";
                break;
            case WEDNESDAY:
                dayOfWeek = "周三";
                break;
            case THURSDAY:
                dayOfWeek = "周四";
                break;
            case FRIDAY:
                dayOfWeek = "周五";
                break;
            case SATURDAY:
                dayOfWeek = "周六";
                break;
            case SUNDAY:
                dayOfWeek = "周日";
                break;
            default:
                dayOfWeek = "异常";
                break;
        }

        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStr = String.format("%s %s", current.format(formatter1), dayOfWeek);
        String dateParam = current.format(formatter2);

        HashMap<String, Object> data = new HashMap<>();

        data.put("state", "ok");
        data.put("Time", dateStr);
        data.put("dateParam", dateParam);
        return data;
    }

    @Override
    public Map<String, Object> getRegistrationsToday(String dateParam, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Patient patient = JWTUtils.getPatientFromToken(token);

        HashMap<String, Object> data = new HashMap<>();

        ArrayList<RegistrationMap> list = (ArrayList<RegistrationMap>)registrationMapper.getRegistrationsMapByPatientAtDate(dateParam, patient.getId());
        if (list.isEmpty()) {
            data.put("您今日没有预约就诊", -1);
            return data;
        }

        for (RegistrationMap item : list) {
            int lineUpCount = registrationMapper.getLineUpCount(item.getVisit_date(), item.getDoctor_id());
            String message;
            if (lineUpCount == 0) {
                message = String.format("%s %s %s，已排到您，请尽快到%s %s诊室就诊", item.getDep_name(), item.getDoctor_name(), item.getDoctor_title(),
                        item.getDep_name(), item.getDoctor_name());
            }
            else {
                message = String.format("排队中，您还需等待：%d 人", lineUpCount);
            }
            data.put(message, lineUpCount);
        }

        return data;
    }

    @Override
    public Map<String, Object> loginHandle(String id, String password, HttpServletRequest request) {

        Patient patient = patientMapper.getPatient(id);

        HashMap<String, Object> map = new HashMap<>();

        if (patient != null && password.equals(patient.getPassword())) {
            LocalDate birthdate = patient.getBirthdate().toLocalDate();
            int age = Period.between(birthdate, LocalDate.now()).getYears();
            patient.setAge(age);

            Map<String, Object> payLoad = new HashMap<>();
            payLoad.put("id", patient.getId());
            payLoad.put("name", patient.getName());
            payLoad.put("sex", patient.getSex());
            payLoad.put("birthdate", patient.getBirthdate());
            payLoad.put("age", age);

            String token = JWTUtils.createToken(payLoad);
//            redisTemplate.opsForValue().set(patient.getId(), token);
//            redisTemplate.expire(patient.getId(), JWTUtils.keepTime, TimeUnit.SECONDS);

            map.put("state", "ok");
            map.put("message", "登录成功");
            map.put("patient_token", token);
            return map;
        }
        else {
            map.put("state", "fail");
            map.put("message", "登陆失败");
            return map;
        }
    }

    @Override
    public Map<String, Object> registerHandle(String id, String name, String sex, String birthdate, String password) {

        Patient patient = patientMapper.getPatient(id);
        HashMap<String, Object> map = new HashMap<>();

        if (patient != null) {
            map.put("state", "duplicated");
            map.put("message", "注册失败，该身份账号已存在");
            return map;
        }

        if (id != null && !id.isEmpty() && name != null && !name.isEmpty()
                &&  sex != null && !sex.isEmpty() && birthdate != null && !birthdate.isEmpty() &&
                password != null && !password.isEmpty() && birthdate.length() == 10) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date realbirthdate = null;
            try {
                realbirthdate = sdf.parse(birthdate);
            }
            catch (ParseException e) {
                e.printStackTrace();
            }

            assert realbirthdate != null;
            LocalDate date1 = realbirthdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            int age = Period.between(date1, LocalDate.now()).getYears();

            java.sql.Date sqlDate = new java.sql.Date(realbirthdate.getTime());

            patient = new Patient(id, name, sex, age, sqlDate, password);
            patientMapper.insertPatient(patient);
            map.put("state", "ok");
            map.put("message", "注册成功，即将返回登录页");
            return map;
        }
        else {
            map.put("state", "fail");
            map.put("message", "注册失败，请检查您的输入是否有误");
            return map;
        }
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Cacheable(value = "PatientsRecords", key = "#patient.id+'-'+#p")
    public PatientRecordsResponse getPatientRecords(String p, Patient patient) {
        int pn;
        if (p == null || p.isEmpty()) {
            pn = 1;
        }
        else {
            pn = Integer.parseInt(p);
        }

        int records_count;
        int total_page_count;
        ArrayList<RegistrationMap> registrations;

        records_count = registrationMapper.getRegistrationsMapByPatientID(patient.getId()).size();
        if (records_count > 0 && records_count % 10 == 0){
            total_page_count = records_count / 10;
        }
        else {
            total_page_count = records_count / 10 + 1;
        }
        Page page = new Page((pn - 1) * 10, 10, pn);

        registrations  = (ArrayList<RegistrationMap>)registrationMapper.getRegistrationsMapByPatientIDForPagination(patient.getId(), page);

        for (int i = 0; i < registrations.size(); i++) {
            registrations.get(i).setVisit_date(registrations.get(i).getVisit_date().substring(0, 10));
        }

        return new PatientRecordsResponse(registrations, records_count, total_page_count, pn);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @CacheEvict(value = "PatientsRecords", allEntries = true)
    public Map<String, Object> cancelRegistration(String id, Patient patient) {
        HashMap<String, Object> map = new HashMap<>();
        Registration updated = registrationMapper.getRegistration(id);
        if (id != null) {
            updated.setStatus(0);
            registrationMapper.updateRegistration(updated);
            map.put("state", "ok");
            map.put("message", "取消预约成功");
            return map;
        }
        map.put("state", "fail");
        map.put("message", "无效的挂号信息");
        return map;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> editHandle(String id, String name, String sex, String birthdate, String password, HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<>();

        if (id != null && !id.isEmpty() && name != null && !name.isEmpty()
                &&  sex != null && !sex.isEmpty() && birthdate != null && !birthdate.isEmpty() &&
                password != null && !password.isEmpty() && birthdate.length() == 10) {

            String token = request.getHeader("Authorization");
            Patient patient = JWTUtils.getPatientFromToken(token);

            if (patient.getId() == null) {
                map.put("state", "fail");
                map.put("message", "修改失败，无效的登录状态");
                return map;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date realbirthdate = null;
            try {
                realbirthdate = sdf.parse(birthdate);
            }
            catch (ParseException e) {
                e.printStackTrace();
            }

            assert realbirthdate != null;
            LocalDate date1 = realbirthdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate now = LocalDate.now();

            int age = Period.between(date1, now).getYears();
            java.sql.Date sqlDate = new java.sql.Date(realbirthdate.getTime());

            Patient patient_update = new Patient(id, name, sex, age, sqlDate, password);
            patientMapper.updatePatient(patient_update);

            map.put("state", "ok");
            map.put("message", "修改成功，请重新登录");
            return map;
        }
        else {
            map.put("state", "fail");
            map.put("message", "修改失败，请检查您的输入是否有空或有误");
            return map;
        }
    }

    @Override
    public PatientArrangementResponse getArrangement() {
        ArrayList<LocalDateTime> times = new ArrayList<>();
        LocalDateTime current = LocalDateTime.now();
        times.add(current);
        for (int i = 1; i < 7; i++) {
            times.add(times.get(0).plusDays(i));
        }

        HashMap<Integer, DateJSON> dates = new HashMap<>();
        String now = null;
        for (int i = 0; i < 7; i++) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
            String dayOfWeek = null;
            switch (times.get(i).getDayOfWeek()) {
                case MONDAY:
                    dayOfWeek = "周一";
                    break;
                case TUESDAY:
                    dayOfWeek = "周二";
                    break;
                case WEDNESDAY:
                    dayOfWeek = "周三";
                    break;
                case THURSDAY:
                    dayOfWeek = "周四";
                    break;
                case FRIDAY:
                    dayOfWeek = "周五";
                    break;
                case SATURDAY:
                    dayOfWeek = "周六";
                    break;
                case SUNDAY:
                    dayOfWeek = "周日";
                    break;
                default:
                    dayOfWeek = "异常";
                    break;
            }
            String dateStr = String.format("%s %s", times.get(i).format(formatter), dayOfWeek);
            if (i == 0) {
                now = dateStr;
            }
            dates.put(i, new DateJSON(dateStr, (times.get(i).toString()).substring(0, 10)));
        }
        ArrayList<Department> departments = (ArrayList<Department>) departmentMapper.getAllDepartments();
        return new PatientArrangementResponse(dates, now, departments);
    }

    @Override
    public ArrayList<DoctorArrangementMap> getDoctorsWorkAtDate(Integer dep_no, String date) {
        return (ArrayList<DoctorArrangementMap>) doctorArrangementMapper.getArrangementsByDepartmentAtDate(dep_no, date);
    }

    @Override
    public String getDoctorDescription(String doctor_id, String date) {
        if (doctor_id != null && date != null) {
            DoctorArrangementMap map = doctorArrangementMapper.getDoctorArrangementMap(date, doctor_id);
            return map.getDescription();
        }
        return "";
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, ParseException.class})
    @CacheEvict(value = "PatientsRecords", allEntries = true)
    public Map<String, Object> registrationSubmit(String doctor_id, String date, HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<>();

        String token = request.getHeader("Authorization");
        Patient patient = JWTUtils.getPatientFromToken(token);

        if (patient.getId() != null && doctor_id != null && !doctor_id.isEmpty()) {

            ArrayList<Registration> collections = (ArrayList<Registration>)registrationMapper.getRegistrationByPatientAtDate(patient.getId(), date);
            for (Registration registration : collections) {
                if (doctor_id.equals(registration.getDoctor_id())  && registration.getStatus() == 1) {
                    map.put("state", "duplicated");
                    map.put("message", "预约失败，请不要重复预约");
                    return map;
                }
            }

            DoctorArrangementMap view = doctorArrangementMapper.getDoctorArrangementMap(date, doctor_id);
            if (view.getRemain() > 0) {
                Date visit_date = null;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    visit_date = sdf.parse(date);
                }
                catch (ParseException e) {
                    e.printStackTrace();
                }

                DoctorArrangement updated = doctorArrangementMapper.getDoctorArrangement(date, doctor_id);
                doctorArrangementMapper.subRemain(updated);
                Registration registration = new Registration(String.valueOf(registrationMapper.getLastID() + 1L), doctor_id, patient.getId(), 1, visit_date);
                registrationMapper.insertRegistration(registration);

                map.put("state", "ok");
                map.put("message", "预约成功");
                return map;
            }
            else {
                map.put("state", "no_remain");
                map.put("message", "预约失败，该医师已无剩余号源");
                return map;
            }
        }
        else {
            map.put("state", "fail");
            map.put("message", "无效的预约信息/身份信息");
            return map;
        }
    }


    @Override
    public int getCounts() {
        return patientMapper.getCounts();
    }
}
