package com.HospitalSystem.Entity;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Registration {//挂号记录
    private String id;
    private String doctor_id;
    private String patient_id;
    private int status;
    private Date visit_date;
}
