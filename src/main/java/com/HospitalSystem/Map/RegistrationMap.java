package com.HospitalSystem.Map;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationMap implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String doctor_id;
    private String doctor_name;
    private String doctor_title;
    private int dep_no;
    private String dep_name;
    private String patient_id;
    private String patient_name;
    private String patient_sex;
    private int patient_age;
    private Date patient_birthdate;
    private int status;
    private String visit_date;
}
