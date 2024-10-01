package com.HospitalSystem.Service;

import com.HospitalSystem.Entity.Admin;
import com.HospitalSystem.Entity.Department;
import com.HospitalSystem.Entity.Doctor;
import com.HospitalSystem.Utils.AdminArrangementResponse;
import com.HospitalSystem.Utils.AdminDoctorInfoResponse;
import com.HospitalSystem.Utils.AdminPatientsDataResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface AdminService {

    Map<String, Object> loginHandle(String id, String password, HttpServletRequest request);

    AdminDoctorInfoResponse doctorinfoInterface(String p, String keyword);

    ArrayList<Doctor> search(String keyword);

    Map<String, Object> deleteDoctor(String id);

    Doctor getDoctor(String id);

    Map<String, Object> updateDoctor(String id, String name, String sex, int dep_no, String title, String password, String description);

    Map<String, Object> insertDoctor(String id, String name, String sex, int dep_no, String title, String password, String description);

    ArrayList<Department> getDepartments();

    Department getDepartment(Integer dep_no);

    ArrayList<Doctor> getDoctors(Integer dep_no);

    Map<String, Object> insertDepartment(Integer dep_no, String dep_name);

    Map<String, Object> updateDepartment(Integer dep_no, String dep_name);

    Map<String, Object> transfer(Integer source, Integer target);

    AdminArrangementResponse getSchedule();

    ArrayList<Doctor> getDoctorsNoWorkAtDate(Integer dep_no, String date);

    ArrayList<Doctor> getDoctorsWorkAtDate(Integer dep_no, String date);

    Map<String, Object> goToWork(String date, String doctor_id, Integer remain);

    Map<String, Object> cancelSchedule(String date, String doctor_id);

    AdminPatientsDataResponse patientManager(String p, String keyword);

    Map<String, Object> resetPassword(String p_id);


}
