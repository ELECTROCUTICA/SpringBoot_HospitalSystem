package com.HospitalSystem.Service;

import com.HospitalSystem.Entity.Page;
import com.HospitalSystem.Entity.Registration;
import com.HospitalSystem.Map.RegistrationMap;

import java.util.List;

public interface RegistrationService {
    void insertRegistration(Registration registration);

    void updateRegistration(Registration registration);

    Registration getRegistration(String id);

    List<Registration> getRegistrationsByPatientID(String patient_id);

    List<Registration> getRegistrationByPatientAtDate(String patient_id, String date);

    List<Registration> getRegistrationsByDoctorID(String doctor_id);

    List<RegistrationMap> getRegistrationsMapByPatientID(String patient_id);

    List<RegistrationMap> getRegistrationsMapByPatientIDForPagination(String patient_id, Page page);

    List<RegistrationMap> getRegistrationsMapByDoctorID(String doctor_id);

    List<RegistrationMap> getRegistrationsMapByPatientAtDate(String date, String patient_id);

    int getLineUpCount(String date, String doctor_id);

    int getCount();

    List<Registration> getAllRegistration();
}
