package com.HospitalSystem.Entity;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {//医生
    private String id;                      //工号，不能是身份证 考虑到登录系统后如果医生的id和患者的id有一致的情况
    private String name;
    private String sex;
    private int dep_no;
    private String dep_name;
    private String title;       //医生职称
    private String password;
    private String description;
}
