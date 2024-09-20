package com.HospitalSystem.Service;

import com.HospitalSystem.Entity.Department;
import com.HospitalSystem.Entity.Doctor;
import com.HospitalSystem.Entity.Page;
import com.HospitalSystem.Mapper.DoctorMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("doctorService")
public class DoctorServiceImplement implements DoctorService {

    @Autowired
    private DoctorMapper doctorMapper;

    public DoctorMapper getDoctorMapper() {
        return this.doctorMapper;
    }

    public void setDoctorMapper(DoctorMapper doctorMapper) {
        this.doctorMapper = doctorMapper;
    }

    @Override
    public void insertDoctor(Doctor doctor) {
        doctorMapper.insertDoctor(doctor);
    }

    @Override
    public void deleteDoctor(Doctor doctor) {
        doctorMapper.deleteDoctor(doctor);
    }

    @Override
    public void updateDoctor(Doctor doctor) {
        doctorMapper.updateDoctor(doctor);
    }

    @Override
    public Doctor getDoctor(String id) {
        return doctorMapper.getDoctor(id);
    }

    @Override
    public List<Doctor> getDoctorsByDepartment(Department department) {
        return doctorMapper.getDoctorsByDepartment(department);
    }

    @Override
    public List<Doctor> getDoctorsByTitle(String title) {
        return doctorMapper.getDoctorsByTitle(title);
    }

    @Override
    public List<Doctor> searchDoctorsByKeyWord(String keyword) {
        return doctorMapper.searchDoctorsByKeyWord(keyword);
    }

    @Override
    public List<Doctor> getDoctorsNoWorkAtDate(int dep_no, String date) {
        return doctorMapper.getDoctorsNoWorkAtDate(dep_no, date);
    }

    @Override
    public List<Doctor> getDoctorsWorkAtDate(int dep_no, String date) {
        return doctorMapper.getDoctorsWorkAtDate(dep_no, date);
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorMapper.getAllDoctors();
    }

    @Override
    public List<Doctor> getDoctorsForPagination(Page page) {
        return doctorMapper.getDoctorsForPagination(page);
    }

    @Override
    public List<Doctor> searchDoctorsForPagination(Page page, String keyword) {
        return doctorMapper.searchDoctorsForPagination(page, keyword);
    }

    @Override
    public int getCounts() {
        return doctorMapper.getCounts();
    }
}
