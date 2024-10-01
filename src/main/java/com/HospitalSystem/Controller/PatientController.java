package com.HospitalSystem.Controller;

import com.HospitalSystem.Entity.*;
import com.HospitalSystem.Map.DoctorArrangementMap;
import com.HospitalSystem.Service.PatientService;
import com.HospitalSystem.Utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.*;


@Controller
@RequestMapping("patient")
public class PatientController {

    @Autowired
    private PatientService patientService;
//    @Autowired
//    private RedisTemplate<Object, Object> redisTemplate;


    @RequestMapping("/interface/getServerTime")          //vue接口
    @ResponseBody
    public Map<String, Object> getServerTime() {
        return patientService.getServerTime();
    }

    @RequestMapping(value="/interface/getRegistrationsToday", method = RequestMethod.GET)           //vue 接口
    @ResponseBody
    public Map<String, Object> getRegistrationsToday(String dateParam, HttpServletRequest request) {
        return patientService.getRegistrationsToday(dateParam, request);
    }

    @RequestMapping("/interface/getPatient")
    @ResponseBody
    public Patient getPatient(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return JWTUtils.getPatientFromToken(token);
    }

    @RequestMapping(value = "/login/loginHandle", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> loginHandle(String id, String password, HttpServletRequest request) {
        return patientService.loginHandle(id, password, request);
    }


    @RequestMapping(value = "/register/registerHandle", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> registerHandle(String id, String name, String sex, String birthdate, String password) {
        return patientService.registerHandle(id, name ,sex, birthdate, password);
    }

    @RequestMapping("/edit/editHandle")
    @ResponseBody
    public Map<String, Object> editHandle(String id, String name, String sex, String birthdate, String password, HttpServletRequest request) {
        return patientService.editHandle(id, name, sex, birthdate, password, request);
    }


    @RequestMapping("/interface/getRecords")
    @ResponseBody
    public PatientRecordsResponse getRecords(String p, HttpServletRequest request) {
        return patientService.getPatientRecords(p, request);
    }


    @RequestMapping("/interface/cancelRegistration")
    @ResponseBody
    public Map<String, Object> cancelRegistration(String id) {
        return patientService.cancelRegistration(id);
    }


    @RequestMapping("/interface/logout")
    @ResponseBody
    public Map<String, Object> logoutInterface(HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("state", "ok");
        map.put("message", "登出完成");
        return map;
    }

    @RequestMapping("/interface/getArrangement")
    @ResponseBody
    public PatientArrangementResponse getArrangement() {
        return patientService.getArrangement();
    }

    @RequestMapping("/registration/getDoctorsWorkAtDate")
    @ResponseBody
    public ArrayList<DoctorArrangementMap> getDoctorsWorkAtDate(Integer dep_no, String date) {
        return patientService.getDoctorsWorkAtDate(dep_no, date);
    }

    @RequestMapping("registration/getDescription")
    @ResponseBody
    public String getDoctorDescription(String doctor_id, String date) {
        return patientService.getDoctorDescription(doctor_id, date);
    }

    //病人提交挂号预约
    @RequestMapping("/registration/submit")
    @ResponseBody
    public Map<String, Object> submit(String doctor_id, String date, HttpServletRequest request) {
        return patientService.registrationSubmit(doctor_id, date, request);
    }
}

