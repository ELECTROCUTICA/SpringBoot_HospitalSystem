package com.HospitalSystem.Utils;

import com.HospitalSystem.Entity.Department;
import com.HospitalSystem.Entity.Doctor;
import com.HospitalSystem.Map.RegistrationMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdminDoctorInfoResponse {
    private ArrayList<Doctor> doctors;
    private ArrayList<Department> departments;
    private int doctors_count;
    private int pages_count;
    private int current;
}
