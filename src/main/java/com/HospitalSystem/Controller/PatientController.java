package com.HospitalSystem.Controller;

import com.HospitalSystem.Entity.*;
import com.HospitalSystem.Map.DoctorArrangementMap;
import com.HospitalSystem.Map.RegistrationMap;
import com.HospitalSystem.Service.DepartmentService;
import com.HospitalSystem.Service.DoctorArrangementService;
import com.HospitalSystem.Service.PatientService;
import com.HospitalSystem.Service.RegistrationService;
import com.HospitalSystem.Utils.JWTUtils;
import com.HospitalSystem.Utils.TokenRemover;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Controller
@RequestMapping("patient")
public class PatientController {

    //所有带有@ResponseBody的方法均为ajax请求的方法

    @Autowired
    private PatientService patientService;
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private DoctorArrangementService doctorArrangementService;
//    @Autowired
//    private RedisTemplate<Object, Object> redisTemplate;

    @RequestMapping("/home")
    public ModelAndView home(HttpServletRequest request) {
//        HttpSession  session = request.getSession(false);
//        Patient patient = (Patient)session.getAttribute("Patient");

        String token = (String)request.getAttribute("patient_token");
        Patient patient = JWTUtils.getPatientFromToken(token);

        ModelAndView mav = new ModelAndView();
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

        HashMap<RegistrationMap, Integer> maps = new HashMap<>();
        ArrayList<RegistrationMap> list = (ArrayList<RegistrationMap>)registrationService.getRegistrationsMapByPatientAtDate(dateParam, patient.getId());
        for (RegistrationMap item : list) {
            int lineUpCount = registrationService.getLineUpCount(item.getVisit_date(), item.getDoctor_id());
            maps.put(item, lineUpCount);
        }

        mav.addObject("Time", dateStr);
        mav.addObject("Registrations", maps);
        mav.setViewName("home");

        return mav;
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/patientHeader")
    public String patientHeader(HttpServletRequest request) {
//        ModelAndView mav = new ModelAndView();

//        String token = (String)request.getAttribute("token");
//        Patient patient = JWTUtils.getPatientFromToken(token);

//        HttpSession session = request.getSession(false);
//        Patient patient = (Patient)session.getAttribute("Patient");
//        mav.addObject("Patient", patient);
//        mav.setViewName("patientHeader");

        return "patientHeader";
    }

    @RequestMapping("/interface/getPatient")
    @ResponseBody
    public Patient getPatient(HttpServletRequest request) {
        String token = (String)request.getAttribute("patient_token");
        return JWTUtils.getPatientFromToken(token);
    }

    @RequestMapping("/patientNav")
    public String patientNav() {
        return "patientNav";
    }


    @RequestMapping(value = "/login/loginHandle", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> loginHandle(String id, String password, HttpServletRequest request, HttpServletResponse response) {
        Patient patient = patientService.getPatient(id);

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

            Cookie cookie = new Cookie("patient_token", token);
            cookie.setPath("/");
            response.addCookie(cookie);

//            HttpSession session = request.getSession(true);
//            session.setAttribute("Patient", patient);
            return map;
        }
        else {
            map.put("state", "fail");
            map.put("message", "登陆失败");
            return map;
        }
    }

    @RequestMapping(value = "/register")
    public String register() {
        return "register";
    }

    @RequestMapping(value = "/register/registerHandle", method = RequestMethod.POST)
    @ResponseBody
    public String registerHandle(String id, String name, String sex, String birthdate, String password) {
        Patient patient = patientService.getPatient(id);
        if (patient != null) return "duplicate";

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
            LocalDate now = LocalDate.now();

            int age = Period.between(date1, now).getYears();

            java.sql.Date sqlDate = new java.sql.Date(realbirthdate.getTime());

            patient = new Patient(id, name, sex, age, sqlDate, password);
            patientService.insertPatient(patient);
            return "ok";
        }
        else {
            return "fail";
        }
    }

    @RequestMapping("/edit")
    public ModelAndView edit(HttpServletRequest request) {
//        HttpSession session = request.getSession(false);
//        Patient patient = (Patient)session.getAttribute("Patient");

        String token = (String)request.getAttribute("patient_token");
        Patient patient = JWTUtils.getPatientFromToken(token);

        ModelAndView mav = new ModelAndView();
        mav.addObject("Patient", patient);
        mav.setViewName("edit");
        return mav;
    }

    @RequestMapping("/edit/editHandle")
    @ResponseBody
    public Map<String, Object> editHandle(String id, String name, String sex, String birthdate, String password, HttpServletRequest request, HttpServletResponse response) {
        HashMap<String, Object> map = new HashMap<>();

        if (id != null && !id.isEmpty() && name != null && !name.isEmpty()
                &&  sex != null && !sex.isEmpty() && birthdate != null && !birthdate.isEmpty() &&
                password != null && !password.isEmpty() && birthdate.length() == 10) {

            String token = (String)request.getAttribute("patient_token");
            Patient patient = JWTUtils.getPatientFromToken(token);

            if (patient == null || patient.getId() == null) {
                map.put("state", "fail");
                map.put("message", "修改失败，无效的登录状态");
                return map;
            }
//            HttpSession session = request.getSession(false);
//            if (session.getAttribute("Patient") == null) {
//                return "fail";
//            }


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
            patientService.updatePatient(patient_update);

            TokenRemover.removeTokenForPatient(request, response);

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

    @RequestMapping("/record")
    public ModelAndView record(String p, HttpServletRequest request) {
//        HttpSession session = request.getSession(false);
//        Patient patient = (Patient)session.getAttribute("Patient");

        String token = (String)request.getAttribute("patient_token");
        Patient patient = JWTUtils.getPatientFromToken(token);

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

        records_count = registrationService.getRegistrationsMapByPatientID(patient.getId()).size();
        if (records_count > 0 && records_count % 10 == 0){
            total_page_count = records_count / 10;
        }
        else {
            total_page_count = records_count / 10 + 1;
        }
        Page page = new Page((pn - 1) * 10, 10, pn);

        registrations  = (ArrayList<RegistrationMap>)registrationService.getRegistrationsMapByPatientIDForPagination(patient.getId(), page);

        for (int i = 0; i < registrations.size(); i++) {
            registrations.get(i).setVisit_date(registrations.get(i).getVisit_date().substring(0, 10));
        }

        ModelAndView mav = new ModelAndView();
        mav.addObject("registrations", registrations);
        mav.addObject("Record_Count", records_count);
        mav.addObject("Page_Count", total_page_count);
        mav.addObject("current", pn);
        mav.setViewName("record");

        return mav;
    }


    @RequestMapping("/record/cancel")
    @ResponseBody
    public void cancel(String id) {
        Registration updated = registrationService.getRegistration(id);
        if (id != null) {
            updated.setStatus(0);
            registrationService.updateRegistration(updated);
        }
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        TokenRemover.removeTokenForPatient(request, response);

//        HttpSession session = request.getSession(false);
//
//        if (session.getAttribute("Patient") != null) {
//            session.removeAttribute("Patient");
//            return "redirect:login";
//        }
        return "redirect:login";
    }

    @RequestMapping("/registration")
    public ModelAndView registration() {
        ModelAndView mav = new ModelAndView();
        ArrayList<LocalDateTime> times = new ArrayList<>();
        LocalDateTime current = LocalDateTime.now();
        times.add(current);
        for (int i = 1; i < 7; i++) {
            times.add(times.get(0).plusDays(i));
        }

        ArrayList<String> realTimes = new ArrayList<>();
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
            realTimes.add(dateStr);

        }
        mav.addObject("Dates", realTimes);
        mav.addObject("Now", realTimes.get(0));
        ArrayList<Department> departments = (ArrayList<Department>) departmentService.getAllDepartments();
        mav.addObject("Departments", departments);
        mav.setViewName("registration");
        return mav;

    }

    @RequestMapping("/registration/getDepartments")
    @ResponseBody
    public ModelAndView getDepartments() {
        ModelAndView mav = new ModelAndView();
        ArrayList<Department> departments = (ArrayList<Department>) departmentService.getAllDepartments();
        mav.addObject("Departments", departments);
        mav.setViewName("registration");
        return mav;
    }

    @RequestMapping("/registration/getDoctorsWorkAtDate")
    @ResponseBody
    public ArrayList<DoctorArrangementMap> getDoctorsWorkAtDate(Integer dep_no, String date) {
        ArrayList<DoctorArrangementMap> maps = (ArrayList<DoctorArrangementMap>)doctorArrangementService.getArrangementsByDepartmentAtDate(dep_no, date);
        return maps;
    }

    @RequestMapping("registration/getDescription")
    @ResponseBody
    public String getDoctorDescription(String doctor_id, String date) {
        if (doctor_id != null && date != null) {
            DoctorArrangementMap map = doctorArrangementService.getDoctorArrangementMap(date, doctor_id);
            return map.getDescription();
        }
        return "";
    }

    //病人提交挂号预约
    @RequestMapping("/registration/submit")
    @ResponseBody
    public Map<String, Object> submit(String doctor_id, String date, HttpServletRequest request) {
//        HttpSession session = request.getSession(false);
//        Patient patient = (Patient) session.getAttribute("Patient");

        HashMap<String, Object> map = new HashMap<>();

        String token = (String)request.getAttribute("patient_token");
        Patient patient = JWTUtils.getPatientFromToken(token);

        if (patient != null && doctor_id != null && !doctor_id.isEmpty() && date.length() == 10) {

            ArrayList<Registration> collections = (ArrayList<Registration>)registrationService.getRegistrationByPatientAtDate(patient.getId(), date);
            for (Registration registration : collections) {
                if (doctor_id.equals(registration.getDoctor_id())  && registration.getStatus() == 1) {
                    map.put("state", "duplicated");
                    map.put("message", "预约失败，请不要重复预约");
                    return map;
                }
            }

            DoctorArrangementMap view = doctorArrangementService.getDoctorArrangementMap(date, doctor_id);
            if (view.getRemain() > 0) {
                Date visit_date = null;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    visit_date = sdf.parse(date);
                }
                catch (ParseException e) {
                    e.printStackTrace();
                }

                int count = registrationService.getCount();
                long id = 241000000000L + count;

                DoctorArrangement updated = doctorArrangementService.getDoctorArrangement(date, doctor_id);
                doctorArrangementService.subRemain(updated);
                Registration registration = new Registration(String.valueOf(id), doctor_id, patient.getId(), 1, visit_date);
                registrationService.insertRegistration(registration);

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
}

