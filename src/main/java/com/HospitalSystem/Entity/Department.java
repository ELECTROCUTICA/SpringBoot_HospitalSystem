package com.HospitalSystem.Entity;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    private int dep_no;
    private String dep_name;
}
