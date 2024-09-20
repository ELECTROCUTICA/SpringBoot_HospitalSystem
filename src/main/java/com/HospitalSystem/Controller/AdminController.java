package com.HospitalSystem.Controller;

import com.HospitalSystem.Entity.*;
import com.HospitalSystem.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("admin")
public class  AdminController {

    //所有带有@ResponseBody的方法均为ajax请求的方法

    @Autowired
    private AdminService adminService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DoctorArrangementService doctorArrangementService;

    @Autowired
    private PatientService patientService;

    @RequestMapping("/login")
    public String adminlogin() {
        return "adminlogin";
    }

    @RequestMapping("")
    public String home() {
        return "adminHome";
    }


    //登录处理
    @RequestMapping("/login/loginHandle")
    @ResponseBody
    public String loginHandle(String id, String password, HttpServletRequest request) {
        Admin admin = adminService.getAdmin(id);

        if (admin != null && password.equals(admin.getPassword())) {
            HttpSession session = request.getSession(true);
            session.setAttribute("Admin", admin);
            return "ok";
        }
        else {
            return "fail";
        }
    }

    //导航栏
    @RequestMapping("/adminNav")
    public ModelAndView adminNav(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();//封装模型数据和视图名称
        HttpSession session = request.getSession(false);
        mav.addObject("Admin", (Admin)session.getAttribute("Admin"));
        mav.setViewName("adminNav");
        return mav;
    }

    @RequestMapping("/interface/getAdmin")
    @ResponseBody
    public Admin getAdmin(HttpServletRequest request) {
        return (Admin)request.getSession(false).getAttribute("Admin");
    }

    //医生信息管理
//    @RequestMapping("/doctorinfo")
//    public ModelAndView doctorinfo() {
//        ModelAndView mav = new ModelAndView();
//        ArrayList<Doctor> doctors = (ArrayList<Doctor>) doctorService.getAllDoctors();
//        mav.addObject("Doctors", doctors);
//
//        ArrayList<Department> departments = (ArrayList<Department>) departmentService.getAllDepartments();
//        mav.addObject("Departments", departments);
//
//        mav.setViewName("doctorinfo");
//
//        return mav;
//    }

    @RequestMapping("/doctorinfo")
    public ModelAndView doctorinfo(String p, String keyword) {
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
            doctors_count = doctorService.searchDoctorsByKeyWord(keyword).size();
            if (doctors_count > 0 && doctors_count % 10 == 0){
                total_page_count = doctors_count / 10;
            }
            else {
                total_page_count = doctors_count / 10 + 1;
            }
            Page page = new Page((pn - 1) * 10, 10, pn);
            doctors = (ArrayList<Doctor>)doctorService.searchDoctorsForPagination(page, keyword);
        }
        else {
            doctors_count = doctorService.getCounts();
            if (doctors_count > 0 && doctors_count % 10 == 0){
                total_page_count = doctors_count / 10;
            }
            else {
                total_page_count = doctors_count / 10 + 1;
            }
            Page page = new Page((pn - 1) * 10, 10, pn);
            doctors = (ArrayList<Doctor>)doctorService.getDoctorsForPagination(page);
        }

        ArrayList<Department> departments = (ArrayList<Department>)departmentService.getAllDepartments();

        ModelAndView mav = new ModelAndView();
        mav.addObject("Doctors", doctors);
        mav.addObject("Doctors_Count", doctors_count);
        mav.addObject("Departments", departments);
        mav.addObject("Page_Count", total_page_count);
        mav.addObject("current", pn);
        mav.setViewName("doctorinfo");
        return mav;

    }

    //医生信息查找
    @RequestMapping("/doctorinfo/search")
    @ResponseBody
    public ArrayList<Doctor> search(String keyword) {
        ArrayList<Doctor> result = (ArrayList<Doctor>)doctorService.searchDoctorsByKeyWord(keyword);
        return result;
    }

    //医生信息删除
    @RequestMapping("/doctorinfo/delete")
    @ResponseBody
    public String delete(String id) {
        Doctor doctor = doctorService.getDoctor(id);
        if (doctor != null) {
            doctorService.deleteDoctor(doctor);
            return "ok";
        }
        else {
            return "fail";
        }
    }

    //返回Doctor对象 ajax用
    @RequestMapping("/doctorinfo/getDoctor")
    @ResponseBody
    public Doctor getDoctor(String id) {
        return doctorService.getDoctor(id);
    }

    //管理医生信息下获取所有科室
    @RequestMapping("/doctorinfo/getDepartment")
    @ResponseBody
    public ModelAndView getDepartment() {
        ModelAndView mav = new ModelAndView();
        ArrayList<Department> departments = (ArrayList<Department>) departmentService.getAllDepartments();
        mav.addObject("Departments", departments);
        mav.setViewName("doctorinfo");
        return mav;
    }


    //修改医生信息
    @RequestMapping("/doctorinfo/update")
    @ResponseBody
    public String edit(String id, String name, String sex, int dep_no, String title, String password, String description) {
        Doctor doctor = doctorService.getDoctor(id);
        if (doctor != null) {
            doctor.setName(name);
            doctor.setSex(sex);
            doctor.setDep_no(dep_no);
            doctor.setDep_name(departmentService.getDepartment(dep_no).getDep_name());
            doctor.setTitle(title);
            doctor.setPassword(password);
            doctor.setDescription(description);
            doctorService.updateDoctor(doctor);
            return "ok";
        }
        else {
            return "fail";
        }
    }

    //新增医生信息
    @RequestMapping("/doctorinfo/insert")
    @ResponseBody
    public String insert(String new_id, String new_name, String new_sex, int new_dep_no, String new_title, String new_password, String new_description) {
        if (new_id != null && new_name != null && new_sex != null && new_title != null &&
        new_password != null && new_description != null) {
            if (doctorService.getDoctor(new_id) == null) {
                Doctor doctor = new Doctor(new_id, new_name, new_sex, new_dep_no, departmentService.getDepartment(new_dep_no).getDep_name(),
                        new_title, new_password, new_description);
                doctorService.insertDoctor(doctor);
                return "ok";
            }
            else return "fail";
        }
        else {
            return "fail";
        }

    }

    //科室页面
    @RequestMapping("/department")
    public ModelAndView department() {
        ModelAndView mav = new ModelAndView();
        ArrayList<Department> departments = (ArrayList<Department>) departmentService.getAllDepartments();
        mav.addObject("Departments", departments);
        mav.setViewName("department");

        return mav;
    }

    //根据科室编号获取科室
    @RequestMapping("/department/getDepartment")
    @ResponseBody
    public Department getDepartment(Integer dep_no) {
        return departmentService.getDepartment(dep_no);
    }

    //查看一个科室下的所有医生
    @RequestMapping("/department/getDoctors")
    @ResponseBody
    public ArrayList<Doctor> getDoctors(Integer dep_no) {
        Department department = departmentService.getDepartment(dep_no);
        ArrayList<Doctor> doctors = (ArrayList<Doctor>)doctorService.getDoctorsByDepartment(department);
        return doctors;
    }

    //创建一个新科室
    @RequestMapping("/department/insert")
    @ResponseBody
    public String insert(Integer new_dep_no, String new_dep_name) {
        if (departmentService.getDepartment(new_dep_no) == null && new_dep_name != null) {
            if (new_dep_no < 0)  {
                return "negative";
            }
            Department department = new Department(new_dep_no, new_dep_name);
            departmentService.insertDepartment(department);
            return "ok";
        }
        return "fail";
    }

    //修改科室信息
    @RequestMapping("/department/update")
    @ResponseBody
    public String update(Integer dep_no, String dep_name) {
        if (dep_no >= 0 && dep_name != null ) {
            Department department = departmentService.getDepartment(dep_no);
            department.setDep_name(dep_name);
            departmentService.updateDepartment(department);
            return "ok";
        }
        return "fail";
    }

    //迁移一个科室的所有医生
    @RequestMapping("/department/transfer")
    @ResponseBody
    public String transfer(Integer source, Integer target) {
        if (source >= 0 && target >= 0) {
            departmentService.transferDoctorsToDepartment(source, target);
            return "ok";
        }
        return "fail";
    }

    //医生排班管理 获取时间
    @RequestMapping("/schedule")
    public ModelAndView schedule() {
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
            String dayOfWeek;
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
        mav.setViewName("schedule");
        return mav;

    }

    @RequestMapping("/schedule/getDepartments")
    @ResponseBody
    public ModelAndView getDepartments() {
        ModelAndView mav = new ModelAndView();
        ArrayList<Department> departments = (ArrayList<Department>) departmentService.getAllDepartments();
        mav.addObject("Departments", departments);
        mav.setViewName("schedule");
        return mav;
    }

    //获取当日某科室的未被安排工作的医生
    @RequestMapping("/schedule/getDoctorsNoWorkAtDate")
    @ResponseBody
    public ArrayList<Doctor> getDoctorsNoWorkAtDate(Integer dep_no, String date) {
        ArrayList<Doctor> noWorkDoctors = (ArrayList<Doctor>)doctorService.getDoctorsNoWorkAtDate(dep_no, date);
        return noWorkDoctors;

    }

    //获取当日某科室的工作医生
    @RequestMapping("/schedule/getDoctorsWorkAtDate")
    @ResponseBody
    public ArrayList<Doctor> getDoctorsWorkAtDate(Integer dep_no, String date) {
        ArrayList<Doctor> workDoctors = (ArrayList<Doctor>)doctorService.getDoctorsWorkAtDate(dep_no, date);
        return workDoctors;

    }

    //安排医生上班
    @RequestMapping("/schedule/goToWork")
    @ResponseBody
    public String goToWork(String date, String doctor_id, Integer remain) {

        DoctorArrangement arrangement = null;
        Date realDate = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            realDate = formatter.parse(date);
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }

        if (doctor_id != null && date != null && remain >= 0) {
            arrangement = new DoctorArrangement(realDate, doctor_id, remain);
            doctorArrangementService.insertArrangement(arrangement);
            return "ok";
        }
        else {
            return "fail";
        }
    }

    //取消医生排班
    @RequestMapping("/schedule/cancel")
    @ResponseBody
    public String cancel(String date, String doctor_id) {
        DoctorArrangement arrangement = doctorArrangementService.getDoctorArrangement(date, doctor_id);
        if (arrangement != null) {
            doctorArrangementService.deleteArrangement(arrangement);
            return "ok";
        }
        return "fail";

    }

    @RequestMapping("/patientManager")
    public ModelAndView patientManager(String p, String keyword) {
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
            patients_count = patientService.searchPatientsByKeyword(keyword).size();
            if (patients_count > 0 && patients_count % 10 == 0){
                total_page_count = patients_count / 10;
            }
            else {
                total_page_count = patients_count / 10 + 1;
            }
            Page page = new Page((pn - 1) * 10, 10, pn);
            patients = (ArrayList<Patient>)patientService.searchPatientsForPagination(page, keyword);
        }
        else {
            patients_count = patientService.getCounts();
            if (patients_count > 0 && patients_count % 10 == 0){
                total_page_count = patients_count / 10;
            }
            else {
                total_page_count = patients_count / 10 + 1;
            }
            Page page = new Page((pn - 1) * 10, 10, pn);
            patients = (ArrayList<Patient>)patientService.getPatientsForPagination(page);
        }


        ModelAndView mav = new ModelAndView();
        mav.addObject("Patients", patients);
        mav.addObject("Patients_Count", patients_count);
        mav.addObject("Page_Count", total_page_count);
        mav.addObject("current", pn);
        mav.setViewName("patientManager");

        return mav;
    }

    @RequestMapping("/patientManager/resetPatientPassword")
    @ResponseBody
    public Map<String, Object> resetPatientPassword(String p_id) {
        HashMap<String, Object> map = new HashMap<>();
        if (p_id.equals("") || p_id == null) {
            map.put("status", "fail");
            map.put("msg", "重置失败，找不到ID为" + p_id + "的病人账户");
        }
        else {
            Patient patient = patientService.getPatient(p_id);
            patient.setPassword("123456");
            patientService.updatePatient(patient);
            map.put("status", "ok");
            map.put("msg", "用户" + p_id + "的密码已重置为：123456");
        }
        return map;
    }

    //管理员登出
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session.getAttribute("Admin") != null) {
            session.removeAttribute("Admin");
            return "redirect:login";
        }
        return "logout";
    }

}
