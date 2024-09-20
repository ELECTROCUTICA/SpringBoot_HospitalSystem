package com.HospitalSystem.Service;


import com.HospitalSystem.Entity.Department;
import com.HospitalSystem.Entity.Doctor;
import com.HospitalSystem.Mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("departmentService")
public class DepartmentServiceImplement implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    public DepartmentMapper getDepartmentMapper() {
        return this.departmentMapper;
    }

    public void setDepartmentMapper(DepartmentMapper departmentMapper) {
        this.departmentMapper = departmentMapper;
    }


    @Override
    public void insertDepartment(Department department) {
        departmentMapper.insertDepartment(department);
    }

    @Override
    public void updateDepartment(Department department) {
        departmentMapper.updateDepartment(department);
    }

    @Override
    public Department getDepartment(int dep_no) {
        return departmentMapper.getDepartment(dep_no);
    }

    @Override
    public List<Doctor> getDoctorsByDepartmentNo(int dep_no) {
        return departmentMapper.getDoctorsByDepartmentNo(dep_no);
    }

    @Override
    public List<Doctor> getDoctorsByDepartmentName(String dep_name) {
        return departmentMapper.getDoctorsByDepartmentName(dep_name);
    }

    @Override
    public void transferDoctorsToDepartment(int source, int target) {
        departmentMapper.transferDoctorsToDepartment(source, target);
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentMapper.getAllDepartments();
    }
}
