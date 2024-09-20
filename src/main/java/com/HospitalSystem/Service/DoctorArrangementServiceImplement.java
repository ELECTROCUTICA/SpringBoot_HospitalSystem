package com.HospitalSystem.Service;


import com.HospitalSystem.Entity.Doctor;
import com.HospitalSystem.Entity.DoctorArrangement;
import com.HospitalSystem.Map.DoctorArrangementMap;
import com.HospitalSystem.Mapper.DoctorArrangementMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("doctorArrangementService")
public class DoctorArrangementServiceImplement implements DoctorArrangementService {


    @Autowired
    private DoctorArrangementMapper doctorArrangementMapper;

    public DoctorArrangementMapper getDoctorArrangementMapper() {
        return this.doctorArrangementMapper;
    }

    public void setDoctorArrangementMapper(DoctorArrangementMapper doctorArrangementMapper) {
        this.doctorArrangementMapper = doctorArrangementMapper;
    }

    @Override
    public void insertArrangement(DoctorArrangement arrangement) {
        doctorArrangementMapper.insertArrangement(arrangement);
    }

    @Override
    public void deleteArrangement(DoctorArrangement arrangement) {
        doctorArrangementMapper.deleteArrangement(arrangement);
    }

    @Override
    public void subRemain(DoctorArrangement arrangement) {
        doctorArrangementMapper.subRemain(arrangement);
    }

    @Override
    public List<DoctorArrangement> getDoctorArrangementByDate(Date date) {
        return doctorArrangementMapper.getDoctorArrangementByDate(date);
    }

    @Override
    public List<DoctorArrangement> getDoctorArrangementByDoctor(Doctor doctor) {
        return doctorArrangementMapper.getDoctorArrangementByDoctor(doctor);
    }

    @Override
    public DoctorArrangement getDoctorArrangement(String date, String doctor_id) {
        return doctorArrangementMapper.getDoctorArrangement(date, doctor_id);
    }

    public DoctorArrangementMap getDoctorArrangementMap(String date, String doctor_id) {
        return doctorArrangementMapper.getDoctorArrangementMap(date, doctor_id);
    }

    @Override
    public List<DoctorArrangementMap> getArrangementsByDepartmentAtDate(int dep_no, String date) {
        return doctorArrangementMapper.getArrangementsByDepartmentAtDate(dep_no, date);
    }

    @Override
    public List<DoctorArrangement> getAllDoctorArrangement() {
        return doctorArrangementMapper.getAllDoctorArrangement();
    }
}
