package com.HospitalSystem.Mapper;

import com.HospitalSystem.Entity.Page;
import com.HospitalSystem.Entity.Patient;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PatientMapper {

    void insertPatient(Patient patient);

    void updatePatient(Patient patient);

    Patient getPatient(String id);

    List<Patient> searchPatientsByKeyword(String keyword);

    List<Patient> getPatientsForPagination(Page page);

    List<Patient> searchPatientsForPagination(Page page, String keyword);

    int getCounts();

    List<Patient> getAllPatients();
}
