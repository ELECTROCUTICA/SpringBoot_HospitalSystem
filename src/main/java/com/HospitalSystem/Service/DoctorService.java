package com.HospitalSystem.Service;

import com.HospitalSystem.Entity.Doctor;
import com.HospitalSystem.Map.RegistrationMap;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

public interface DoctorService {

    List<Doctor> getDoctorsWorkAtDate(int dep_no, String date);

    /////////////////////////////////////////////////////////////////////////


    Map<String, Object> doctorLoginHandle(String id, String password, HttpServletRequest request);

    Map<Integer, RegistrationMap> getPatientsList(HttpServletRequest request);

    Map<String, Object> changingStatus(String id, int status);
}
