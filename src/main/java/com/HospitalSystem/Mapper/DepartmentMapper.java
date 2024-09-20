package com.HospitalSystem.Mapper;

import com.HospitalSystem.Entity.Department;
import com.HospitalSystem.Entity.Doctor;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface DepartmentMapper {

    void insertDepartment(Department department);

    void updateDepartment(Department department);

    Department getDepartment(int dep_no);

    List<Doctor> getDoctorsByDepartmentNo(int dep_no);

    List<Doctor> getDoctorsByDepartmentName(String dep_name);

    void transferDoctorsToDepartment(int source, int target);

    List<Department> getAllDepartments();
}
