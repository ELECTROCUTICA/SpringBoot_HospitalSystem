package com.HospitalSystem.Entity;

import lombok.*;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorArrangement {//医生排班
    private Date date;
    private String doctor_id;
    private int remain;                         //剩余号源数量

}
