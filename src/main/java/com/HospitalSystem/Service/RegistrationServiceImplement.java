package com.HospitalSystem.Service;

import com.HospitalSystem.Entity.Page;
import com.HospitalSystem.Entity.Registration;
import com.HospitalSystem.Map.RegistrationMap;
import com.HospitalSystem.Mapper.RegistrationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("registrationService")
public class RegistrationServiceImplement implements RegistrationService {
    @Autowired
    private RegistrationMapper registrationMapper;

    public RegistrationMapper getRegistrationMapper() {
        return this.registrationMapper;
    }

    public void setRegistrationMapper(RegistrationMapper registrationMapper) {
        this.registrationMapper = registrationMapper;
    }

    @Override
    public void insertRegistration(Registration registration) {
        registrationMapper.insertRegistration(registration);
    }

    @Override
    public void updateRegistration(Registration registration) {
        registrationMapper.updateRegistration(registration);
    }

    @Override
    public Registration getRegistration(String id) {
        return registrationMapper.getRegistration(id);
    }

    @Override
    public List<Registration> getRegistrationsByPatientID(String patient_id) {
        return registrationMapper.getRegistrationsByPatientID(patient_id);
    }

    @Override
    public List<Registration> getRegistrationByPatientAtDate(String patient_id, String date) {
        return registrationMapper.getRegistrationByPatientAtDate(patient_id, date);
    }

    @Override
    public List<Registration> getRegistrationsByDoctorID(String doctor_id) {
        return registrationMapper.getRegistrationsByDoctorID(doctor_id);
    }

    @Override
    public List<RegistrationMap> getRegistrationsMapByPatientID(String patient_id) {
        return registrationMapper.getRegistrationsMapByPatientID(patient_id);
    }

    @Override
    public List<RegistrationMap> getRegistrationsMapByPatientIDForPagination(String patient_id, Page page) {
        return registrationMapper.getRegistrationsMapByPatientIDForPagination(patient_id, page);
    }

    @Override
    public List<RegistrationMap> getRegistrationsMapByDoctorID(String doctor_id) {
        return registrationMapper.getRegistrationsMapByDoctorID(doctor_id);
    }

    @Override
    public List<RegistrationMap> getRegistrationsMapByPatientAtDate(String date, String patient_id) {
        return registrationMapper.getRegistrationsMapByPatientAtDate(date, patient_id);
    }

    @Override
    public int getLineUpCount(String date, String doctor_id) {
        return registrationMapper.getLineUpCount(date, doctor_id);
    }

    @Override
    public int getCount() {
        return registrationMapper.getCount();
    }

    @Override
    public List<Registration> getAllRegistration() {
        return registrationMapper.getAllRegistration();
    }
}
