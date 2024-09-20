package com.HospitalSystem.Service;

import com.HospitalSystem.Entity.Department;
import com.HospitalSystem.Entity.Doctor;

import java.util.List;

public interface DepartmentService {
    void insertDepartment(Department department);

    void updateDepartment(Department department);

    Department getDepartment(int dep_no);

    List<Doctor> getDoctorsByDepartmentNo(int dep_no);

    List<Doctor> getDoctorsByDepartmentName(String dep_name);

    void transferDoctorsToDepartment(int source, int target);

    List<Department> getAllDepartments();
}
