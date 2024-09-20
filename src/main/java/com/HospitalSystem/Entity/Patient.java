package com.HospitalSystem.Entity;

import lombok.*;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patient {//患者
    private String id;
    private String name;
    private String sex;
    private int age;
    private Date birthdate;
    private String password;
}
