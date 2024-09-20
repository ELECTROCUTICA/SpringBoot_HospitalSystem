package com.HospitalSystem.Mapper;

import com.HospitalSystem.Entity.Department;
import com.HospitalSystem.Entity.Doctor;
import com.HospitalSystem.Entity.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface DoctorMapper {

    void insertDoctor(Doctor doctor);

    void deleteDoctor(Doctor doctor);

    void updateDoctor(Doctor doctor);

    Doctor getDoctor(String id);

    List<Doctor> getDoctorsByDepartment(Department department);

    List<Doctor> getDoctorsByTitle(String title);

    List<Doctor> searchDoctorsByKeyWord(String keyword);

    List<Doctor> getDoctorsNoWorkAtDate(int dep_no, String date);

    List<Doctor> getDoctorsWorkAtDate(int dep_no, String date);

    List<Doctor> getAllDoctors();

    List<Doctor> getDoctorsForPagination(Page page);

    List<Doctor> searchDoctorsForPagination(Page page, String keyword);

    int getCounts();

}
