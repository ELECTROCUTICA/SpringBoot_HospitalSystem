package com.HospitalSystem.Service;

import com.HospitalSystem.Entity.Doctor;
import com.HospitalSystem.Entity.DoctorArrangement;
import com.HospitalSystem.Map.DoctorArrangementMap;

import java.util.Date;
import java.util.List;

public interface DoctorArrangementService {
    void insertArrangement(DoctorArrangement arrangement);

    void deleteArrangement(DoctorArrangement arrangement);

    void subRemain(DoctorArrangement arrangement);

    List<DoctorArrangement> getDoctorArrangementByDate(Date date);

    List<DoctorArrangement> getDoctorArrangementByDoctor(Doctor doctor);

    DoctorArrangement getDoctorArrangement(String date, String doctor_id);

    DoctorArrangementMap getDoctorArrangementMap(String date, String doctor_id);

    List<DoctorArrangementMap> getArrangementsByDepartmentAtDate(int dep_no, String date);

    List<DoctorArrangement> getAllDoctorArrangement();
}
