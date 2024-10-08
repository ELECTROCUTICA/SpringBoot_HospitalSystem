package com.HospitalSystem.Service;

import com.HospitalSystem.Entity.*;
import com.HospitalSystem.Mapper.*;
import com.HospitalSystem.Utils.AdminArrangementResponse;
import com.HospitalSystem.Utils.AdminDoctorInfoResponse;
import com.HospitalSystem.Utils.AdminPatientsDataResponse;
import com.HospitalSystem.Utils.DateJSON;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service("adminService")
public class AdminServiceImplement implements AdminService {

    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private DoctorMapper doctorMapper;
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private DoctorArrangementMapper doctorArrangementMapper;
    @Autowired
    private PatientMapper patientMapper;


    @Override
    public Map<String, Object> loginHandle(String id, String password, HttpServletRequest request) {
        Admin admin = adminMapper.getAdmin(id);

        HashMap<String, Object> map = new HashMap<>();
        if (admin != null && password.equals(admin.getPassword())) {
            HttpSession session = request.getSession(true);
            session.setAttribute("Admin", admin);
            map.put("state", "ok");
            map.put("message", "登陆成功");
            return map;
        }
        else {
            map.put("state", "fail");
            map.put("message", "登陆失败");
            return map;
        }
    }

    @Override
    public AdminDoctorInfoResponse doctorinfoInterface(String p, String keyword) {
        int pn;
        if (p == null || p.isEmpty()) {
            pn = 1;
        }
        else {
            pn = Integer.parseInt(p);
        }

        int doctors_count;
        int total_page_count;
        ArrayList<Doctor> doctors;
        if (keyword != null && !keyword.isEmpty()) {
            doctors_count = doctorMapper.searchDoctorsByKeyWord(keyword).size();
            if (doctors_count > 0 && doctors_count % 10 == 0){
                total_page_count = doctors_count / 10;
            }
            else {
                total_page_count = doctors_count / 10 + 1;
            }
            Page page = new Page((pn - 1) * 10, 10, pn);
            doctors = (ArrayList<Doctor>)doctorMapper.searchDoctorsForPagination(page, keyword);
        }
        else {
            doctors_count = doctorMapper.getCounts();
            if (doctors_count > 0 && doctors_count % 10 == 0){
                total_page_count = doctors_count / 10;
            }
            else {
                total_page_count = doctors_count / 10 + 1;
            }
            Page page = new Page((pn - 1) * 10, 10, pn);
            doctors = (ArrayList<Doctor>)doctorMapper.getDoctorsForPagination(page);
        }

        ArrayList<Department> departments = (ArrayList<Department>)departmentMapper.getAllDepartments();

        return new AdminDoctorInfoResponse(doctors, departments, doctors_count, total_page_count, pn);
    }

    @Override
    public ArrayList<Doctor> search(String keyword) {
        return (ArrayList<Doctor>)doctorMapper.searchDoctorsByKeyWord(keyword);
    }

    @Override
    public Map<String, Object> deleteDoctor(String id) {
        Doctor doctor = doctorMapper.getDoctor(id);
        HashMap<String, Object> map = new HashMap<>();
        if (doctor != null) {
            doctorMapper.deleteDoctor(doctor);
            map.put("state", "ok");
            map.put("message", "删除医生信息成功");
        }
        else {
            map.put("state", "fail");
            map.put("message", "删除失败");
        }
        return map;
    }

    @Override
    public Doctor getDoctor(String id) {
        return doctorMapper.getDoctor(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> updateDoctor(String id, String name, String sex, int dep_no, String title, String password, String description) {
        HashMap<String, Object> map =  new HashMap<>();
        Doctor doctor = doctorMapper.getDoctor(id);
        if (doctor != null) {
            doctor.setName(name);
            doctor.setSex(sex);
            doctor.setDep_no(dep_no);
            doctor.setDep_name(departmentMapper.getDepartment(dep_no).getDep_name());
            doctor.setTitle(title);
            doctor.setPassword(password);
            doctor.setDescription(description);
            doctorMapper.updateDoctor(doctor);
            map.put("state", "ok");
            map.put("message", "修改成功");
            return map;
        }
        else {
            map.put("state", "fail");
            map.put("message", "修改失败");
            return map;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> insertDoctor(String id, String name, String sex, int dep_no, String title, String password, String description) {
        HashMap<String, Object> map = new HashMap<>();
        if (id != null && name != null && sex != null && title != null &&
                password != null && description != null) {
            if (doctorMapper.getDoctor(id) == null) {
                Doctor doctor = new Doctor(id, name, sex, dep_no, departmentMapper.getDepartment(dep_no).getDep_name(),
                        title, password, description);
                doctorMapper.insertDoctor(doctor);
                map.put("state", "ok");
                map.put("message", "创建医生信息完成");
            }
            else {
                map.put("state", "fail");
                map.put("message", "新建失败，请查看是否已存在该医生");
            }
        }
        else {
            map.put("state", "fail");
            map.put("message", "新建失败，请查看输入信息是否有效");
        }
        return map;
    }

    @Override
    public ArrayList<Department> getDepartments() {
        return (ArrayList<Department>) departmentMapper.getAllDepartments();
    }

    @Override
    public Department getDepartment(Integer dep_no) {
        return departmentMapper.getDepartment(dep_no);
    }

    @Override
    public ArrayList<Doctor> getDoctors(Integer dep_no) {
        Department department = departmentMapper.getDepartment(dep_no);
        return (ArrayList<Doctor>)doctorMapper.getDoctorsByDepartment(department);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> insertDepartment(Integer dep_no, String dep_name) {
        HashMap<String, Object> map = new HashMap<>();
        if (departmentMapper.getDepartment(dep_no) == null && dep_name != null) {
            if (dep_no < 0)  {
                map.put("state", "fail");
                map.put("message", "非法的科室编号");
                return map;
            }
            Department department = new Department(dep_no, dep_name);
            departmentMapper.insertDepartment(department);
            map.put("state", "ok");
            map.put("message", "创建科室成功");
            return map;
        }
        map.put("state", "fail");
        map.put("message", "创建失败，请检查是否存在重复的科室");
        return map;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> updateDepartment(Integer dep_no, String dep_name) {
        HashMap<String, Object> map = new HashMap<>();
        if (dep_no >= 0 && dep_name != null ) {
            Department department = departmentMapper.getDepartment(dep_no);
            department.setDep_name(dep_name);
            departmentMapper.updateDepartment(department);
            map.put("state", "ok");
            map.put("message", "修改成功");
            return map;
        }
        map.put("state", "fail");
        map.put("message", "修改失败");
        return map;
    }

    @Override
    public Map<String, Object> transfer(Integer source, Integer target) {
        HashMap<String, Object> map = new HashMap<>();
        if (source >= 0 && target >= 0) {
            departmentMapper.transferDoctorsToDepartment(source, target);
            map.put("state", "ok");
            map.put("message", "迁移完成");
            return map;
        }
        map.put("state", "fail");
        map.put("message", "迁移失败");
        return map;
    }

    @Override
    public AdminArrangementResponse getSchedule() {
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
        return new AdminArrangementResponse(dates, now, departments);
    }

    @Override
    public ArrayList<Doctor> getDoctorsNoWorkAtDate(Integer dep_no, String date) {
        return (ArrayList<Doctor>)doctorMapper.getDoctorsNoWorkAtDate(dep_no, date);
    }

    @Override
    public ArrayList<Doctor> getDoctorsWorkAtDate(Integer dep_no, String date) {
        return (ArrayList<Doctor>)doctorMapper.getDoctorsWorkAtDate(dep_no, date);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> goToWork(String date, String doctor_id, Integer remain) {
        HashMap<String, Object> map = new HashMap<>();

        DoctorArrangement arrangement;
        Date realDate;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            realDate = formatter.parse(date);
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }

        if (doctor_id != null && date != null && remain > 0) {
            arrangement = new DoctorArrangement(realDate, doctor_id, remain);
            doctorArrangementMapper.insertArrangement(arrangement);
            map.put("state", "ok");
            map.put("message", "排班成功");
        }
        else {
            map.put("state", "fail");
            map.put("message", "排班失败");
        }
        return map;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> cancelSchedule(String date, String doctor_id) {
        HashMap<String, Object> map = new HashMap<>();
        DoctorArrangement arrangement = doctorArrangementMapper.getDoctorArrangement(date, doctor_id);
        if (arrangement != null) {
            doctorArrangementMapper.deleteArrangement(arrangement);
            map.put("state", "ok");
            map.put("message", "取消成功");
            return map;
        }
        map.put("state", "fail");
        map.put("message", "取消失败");
        return map;
    }

    @Override
    public AdminPatientsDataResponse patientManager(String p, String keyword) {
        int pn;
        if (p == null || p.isEmpty()) {
            pn = 1;
        }
        else {
            pn = Integer.parseInt(p);
        }

        int patients_count;
        int total_page_count;
        ArrayList<Patient> patients;
        if (keyword != null && !keyword.isEmpty()) {
            patients_count = patientMapper.searchPatientsByKeyword(keyword).size();
            if (patients_count > 0 && patients_count % 10 == 0){
                total_page_count = patients_count / 10;
            }
            else {
                total_page_count = patients_count / 10 + 1;
            }
            Page page = new Page((pn - 1) * 10, 10, pn);
            patients = (ArrayList<Patient>)patientMapper.searchPatientsForPagination(page, keyword);
        }
        else {
            patients_count = patientMapper.getCounts();
            if (patients_count > 0 && patients_count % 10 == 0){
                total_page_count = patients_count / 10;
            }
            else {
                total_page_count = patients_count / 10 + 1;
            }
            Page page = new Page((pn - 1) * 10, 10, pn);
            patients = (ArrayList<Patient>)patientMapper.getPatientsForPagination(page);
        }

        return new AdminPatientsDataResponse(patients, patients_count, total_page_count, pn);

    }

    @Override
    public Map<String, Object> resetPassword(String p_id) {
        HashMap<String, Object> map = new HashMap<>();
        if (p_id.isEmpty() || p_id == null) {
            map.put("status", "fail");
            map.put("message", "重置失败，找不到ID为" + p_id + "的病人账户");
        }
        else {
            Patient patient = patientMapper.getPatient(p_id);
            patient.setPassword("123456");
            patientMapper.updatePatient(patient);
            map.put("status", "ok");
            map.put("message", "用户" + p_id + "的密码已重置为：123456");
        }
        return map;
    }
}
