package com.HospitalSystem.Controller;

import com.HospitalSystem.Entity.*;
import com.HospitalSystem.Service.*;
import com.HospitalSystem.Utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    //登录处理
    @RequestMapping("/login/loginHandle")
    @ResponseBody
    public Map<String, Object> loginHandle(String id, String password, HttpServletRequest request) {
        return adminService.loginHandle(id, password, request);
    }

    @RequestMapping("/interface/getAdmin")
    @ResponseBody
    public Admin getAdmin(HttpServletRequest request) {
        return (Admin)request.getSession(false).getAttribute("Admin");
    }

    @RequestMapping("/interface/doctorinfo")
    @ResponseBody
    public AdminDoctorInfoResponse doctorinfoInterface(String p, String keyword) {
        return adminService.doctorinfoInterface(p, keyword);
    }

    //医生信息查找
    @RequestMapping("/doctorinfo/search")
    @ResponseBody
    public ArrayList<Doctor> search(String keyword) {
        return adminService.search(keyword);
    }

    //医生信息删除
    @RequestMapping("/doctorinfo/delete")
    @ResponseBody
    public Map<String, Object> delete(String id) {
        return adminService.deleteDoctor(id);
    }

    //返回Doctor对象
    @RequestMapping("/doctorinfo/getDoctor")
    @ResponseBody
    public Doctor getDoctor(String id) {
        return adminService.getDoctor(id);
    }

    //修改医生信息
    @RequestMapping("/doctorinfo/update")
    @ResponseBody
    public Map<String, Object> updateDoctor(String id, String name, String sex, int dep_no, String title, String password, String description) {
        return adminService.updateDoctor(id, name, sex, dep_no, title, password, description);
    }

    //新增医生信息
    @RequestMapping("/doctorinfo/insert")
    @ResponseBody
    public Map<String, Object> insertDoctor(String id, String name, String sex, int dep_no, String title, String password, String description) {
        return adminService.insertDoctor(id, name, sex, dep_no, title, password, description);
    }

    @RequestMapping("/interface/getDepartments")
    @ResponseBody
    public ArrayList<Department> getDepartments() {
        return adminService.getDepartments();
    }

    //根据科室编号获取科室
    @RequestMapping("/department/getDepartment")
    @ResponseBody
    public Department getDepartment(Integer dep_no) {
        return adminService.getDepartment(dep_no);
    }

    //查看一个科室下的所有医生
    @RequestMapping("/department/getDoctors")
    @ResponseBody
    public ArrayList<Doctor> getDoctors(Integer dep_no) {
        return adminService.getDoctors(dep_no);
    }

    //创建一个新科室
    @RequestMapping("/department/insert")
    @ResponseBody
    public Map<String, Object> insert(Integer dep_no, String dep_name) {
        return adminService.insertDepartment(dep_no, dep_name);
    }

    //修改科室信息
    @RequestMapping("/department/update")
    @ResponseBody
    public Map<String, Object> update(Integer dep_no, String dep_name) {
        return adminService.updateDepartment(dep_no, dep_name);
    }

    //迁移一个科室的所有医生
    @RequestMapping("/department/transfer")
    @ResponseBody
    public Map<String, Object> transfer(Integer source, Integer target) {
        return adminService.transfer(source, target);
    }

    @RequestMapping("/interface/getSchedule")
    @ResponseBody
    public AdminArrangementResponse getSchedule() {
        return adminService.getSchedule();
    }


    //获取当日某科室的未被安排工作的医生
    @RequestMapping("/schedule/getDoctorsNoWorkAtDate")
    @ResponseBody
    public ArrayList<Doctor> getDoctorsNoWorkAtDate(Integer dep_no, String date) {
        return adminService.getDoctorsNoWorkAtDate(dep_no, date);
    }

    //获取当日某科室的工作医生
    @RequestMapping("/schedule/getDoctorsWorkAtDate")
    @ResponseBody
    public ArrayList<Doctor> getDoctorsWorkAtDate(Integer dep_no, String date) {
        return adminService.getDoctorsWorkAtDate(dep_no, date);
    }

    //安排医生上班
    @RequestMapping("/schedule/goToWork")
    @ResponseBody
    public Map<String, Object> goToWork(String date, String doctor_id, Integer remain) {
        return adminService.goToWork(date, doctor_id, remain);
    }

    //取消医生排班
    @RequestMapping("/schedule/cancel")
    @ResponseBody
    public Map<String, Object> cancel(String date, String doctor_id) {
        return adminService.cancelSchedule(date, doctor_id);
    }

    @RequestMapping("/interface/patientManager")
    @ResponseBody
    public AdminPatientsDataResponse patientManager(String p, String keyword) {
        return adminService.patientManager(p, keyword);
    }

    @RequestMapping("/interface/resetPassword")
    @ResponseBody
    public Map<String, Object> resetPassword(String p_id) {
        return adminService.resetPassword(p_id);
    }

    @RequestMapping("/interface/logout")
    @ResponseBody
    public String logoutInterface(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session.getAttribute("Admin") != null) {
            session.removeAttribute("Admin");
        }
        return "登出成功";
    }

}
