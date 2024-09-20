package com.HospitalSystem.Service;

import com.HospitalSystem.Entity.Page;
import com.HospitalSystem.Entity.Patient;
import com.HospitalSystem.Mapper.PatientMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("patientService")
public class PatientServiceImplement implements PatientService {
    @Autowired
    private PatientMapper patientMapper;

    public PatientMapper getPatientMapper() {
        return this.patientMapper;
    }

    public void setPatientMapper(PatientMapper patientMapper) {
        this.patientMapper = patientMapper;
    }

    @Override
    public void insertPatient(Patient patient) {
        patientMapper.insertPatient(patient);
    }

    @Override
    public void updatePatient(Patient patient) {
        patientMapper.updatePatient(patient);
    }

    @Override
    public Patient getPatient(String id) {
        return patientMapper.getPatient(id);
    }

    @Override
    public List<Patient> getAllPatients() {
        return patientMapper.getAllPatients();
    }

    @Override
    public List<Patient> searchPatientsByKeyword(String keyword) {
        return patientMapper.searchPatientsByKeyword(keyword);
    }

    @Override
    public List<Patient> getPatientsForPagination(Page page) {
        return patientMapper.getPatientsForPagination(page);
    }

    @Override
    public List<Patient> searchPatientsForPagination(Page page, String keyword) {
        return patientMapper.searchPatientsForPagination(page, keyword);
    }

    @Override
    public int getCounts() {
        return patientMapper.getCounts();
    }
}
