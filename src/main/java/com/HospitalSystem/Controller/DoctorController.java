package com.HospitalSystem.Controller;

import com.HospitalSystem.Entity.Doctor;
import com.HospitalSystem.Map.RegistrationMap;
import com.HospitalSystem.Service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @RequestMapping("/login/loginHandle")
    @ResponseBody
    public Map<String, Object> doctorLoginHandle(String id, String password, HttpServletRequest request) {
        return doctorService.doctorLoginHandle(id, password, request);
    }

    @RequestMapping("/interface/getDoctor")
    @ResponseBody
    public Doctor getDoctor(HttpServletRequest request) {
        return (Doctor)request.getSession(false).getAttribute("Doctor");
    }

    //获取病人就诊列表
    @RequestMapping("/interface/getPatientsList")
    @ResponseBody
    public Map<Integer, RegistrationMap> getPatientsList(HttpServletRequest request) {
        return doctorService.getPatientsList(request);
    }

    @RequestMapping("/interface/logout")
    @ResponseBody
    public Map<String, Object> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Map<String, Object> map = new HashMap<>();
        if (session.getAttribute("Doctor") != null) {
            session.removeAttribute("Doctor");
            map.put("state", "ok");
            map.put("message", "登出成功");
            return map;
        }
        map.put("state", "fail");
        map.put("message", "登出失败，无效的登录信息");
        return map;
    }

    @RequestMapping("/changingStatus")
    @ResponseBody
    public Map<String, Object> changingStatus(String id, int status) {
        return doctorService.changingStatus(id, status);
    }


}
