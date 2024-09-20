package com.HospitalSystem.Controller;

import com.HospitalSystem.Entity.Doctor;
import com.HospitalSystem.Entity.Registration;
import com.HospitalSystem.Map.RegistrationMap;
import com.HospitalSystem.Service.DepartmentService;
import com.HospitalSystem.Service.DoctorService;
import com.HospitalSystem.Service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;

@Controller
@RequestMapping("doctor")
public class DoctorController {

    //所有带有@ResponseBody的方法均为ajax请求的方法

    @Autowired
    private DoctorService doctorService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private RegistrationService registrationService;

    @RequestMapping("/login")
    public String doctorlogin() {
        return "doctorlogin";
    }

    @RequestMapping("/login/loginHandle")
    @ResponseBody
    public String doctorLoginHandle(String id, String password, HttpServletRequest request) {
        Doctor doctor = doctorService.getDoctor(id);

        if (doctor != null && password.equals(doctor.getPassword())) {
            HttpSession session = request.getSession(true);
            doctor.setDep_name((departmentService.getDepartment(doctor.getDep_no())).getDep_name());
            session.setAttribute("Doctor", doctor);
            return "ok";
        }
        else {
            return "fail";
        }
    }

    @RequestMapping("/interface/getDoctor")
    @ResponseBody
    public Doctor getDoctor(HttpServletRequest request) {
        return (Doctor)request.getSession(false).getAttribute("Doctor");
    }

    //医师主页，就诊病人列表
    @RequestMapping("/list")
    public ModelAndView list(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        HttpSession session = request.getSession(false);
        ArrayList<RegistrationMap> list = null;
        HashMap<Integer, RegistrationMap> maps = new HashMap<>();

        Doctor doctor = (Doctor)session.getAttribute("Doctor");

        if (doctor != null) {
            list = (ArrayList<RegistrationMap>)registrationService.getRegistrationsMapByDoctorID(doctor.getId());

            for (int i = 0; i < list.size(); i++) {
                RegistrationMap temp = list.get(i);
                temp.setVisit_date(temp.getVisit_date().substring(0, 10));
                maps.put(i + 1, temp);
            }
        }
        mav.addObject("PatientsList", maps);

        mav.setViewName("list");
        return mav;
    }

    @RequestMapping("logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session.getAttribute("Doctor") != null) {
            session.removeAttribute("Doctor");
            return "redirect:login";
        }
        return "logout";
    }

    @RequestMapping("/changeStatus")
    public void changeStatus(String id, int status) {
        Registration updated = registrationService.getRegistration(id);
        updated.setStatus(status);
        registrationService.updateRegistration(updated);
    }



}
