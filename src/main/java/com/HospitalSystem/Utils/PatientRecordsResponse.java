package com.HospitalSystem.Utils;


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
public class PatientRecordsResponse {
    private ArrayList<RegistrationMap> registrations;
    private int records_count;
    private int pages_count;
    private int current;
}
