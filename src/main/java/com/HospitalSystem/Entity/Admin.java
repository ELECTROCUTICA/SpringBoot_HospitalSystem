package com.HospitalSystem.Entity;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Admin {        //管理员
    private String id;
    private String password;
}
