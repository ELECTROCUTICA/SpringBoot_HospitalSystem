package com.HospitalSystem.Service;

import com.HospitalSystem.Entity.Page;
import com.HospitalSystem.Entity.Patient;

import java.util.List;

public interface PatientService {
    void insertPatient(Patient patient);

    void updatePatient(Patient patient);

    Patient getPatient(String id);

    List<Patient> getAllPatients();

    List<Patient> searchPatientsByKeyword(String keyword);

    List<Patient> getPatientsForPagination(Page page);

    List<Patient> searchPatientsForPagination(Page page, String keyword);

    int getCounts();
}
