package com.HospitalSystem.Map;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorArrangementMap {
    private String doctor_id;
    private String name;
    private int dep_no;
    private String dep_name;
    private String title;
    private String description;
    private String date;
    private int remain;
}
