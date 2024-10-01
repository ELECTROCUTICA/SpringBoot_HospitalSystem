package com.HospitalSystem.Service;

import com.HospitalSystem.Entity.Department;
import com.HospitalSystem.Entity.Doctor;
import com.HospitalSystem.Entity.Page;
import com.HospitalSystem.Entity.Registration;
import com.HospitalSystem.Map.RegistrationMap;
import com.HospitalSystem.Mapper.DepartmentMapper;
import com.HospitalSystem.Mapper.DoctorMapper;
import com.HospitalSystem.Mapper.RegistrationMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("doctorService")
public class DoctorServiceImplement implements DoctorService {

    @Autowired
    private DoctorMapper doctorMapper;
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private RegistrationMapper registrationMapper;
    @Override
    public List<Doctor> getDoctorsWorkAtDate(int dep_no, String date) {
        return doctorMapper.getDoctorsWorkAtDate(dep_no, date);
    }

    @Override
    public Map<String, Object> doctorLoginHandle(String id, String password, HttpServletRequest request) {
        Doctor doctor = doctorMapper.getDoctor(id);
        HashMap<String, Object> map = new HashMap<>();

        if (doctor != null && password.equals(doctor.getPassword())) {
            HttpSession session = request.getSession(true);
            doctor.setDep_name((departmentMapper.getDepartment(doctor.getDep_no())).getDep_name());
            session.setAttribute("Doctor", doctor);
            map.put("state", "ok");
            map.put("message", String.format("%s医生，登入成功", doctor.getName()));
        }
        else {
            map.put("state", "fail");
            map.put("message", "登入失败，请输入正确的职工号和密码");
        }
        return map;
    }

    @Override
    public Map<Integer, RegistrationMap> getPatientsList(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        ArrayList<RegistrationMap> list = null;
        HashMap<Integer, RegistrationMap> map = new HashMap<>();

        Doctor doctor = (Doctor)session.getAttribute("Doctor");

        if (doctor != null) {
            list = (ArrayList<RegistrationMap>)registrationMapper.getRegistrationsMapByDoctorID(doctor.getId());

            for (int i = 0; i < list.size(); i++) {
                RegistrationMap temp = list.get(i);
                temp.setVisit_date(temp.getVisit_date().substring(0, 10));
                map.put(i + 1, temp);
            }
        }
        return map;
    }

    @Override
    public Map<String, Object> changingStatus(String id, int status) {
        HashMap<String, Object> map = new HashMap<>();
        if (id != null) {
            Registration updated = registrationMapper.getRegistration(id);
            updated.setStatus(status);
            registrationMapper.updateRegistration(updated);
            if (status == 2) {
                map.put("state", "ok");
                map.put("message", "成功完成就诊");
                return map;
            }
            else if (status == 0) {
                map.put("state", "ok");
                map.put("message", "成功取消就诊");
                return map;
            }
            else {
                map.put("state", "fail");
                map.put("message", "提交异常");
                return map;
            }
        }
        else {
            map.put("state", "fail");
            map.put("message", "无效的就诊信息");
            return map;
        }
    }


}
