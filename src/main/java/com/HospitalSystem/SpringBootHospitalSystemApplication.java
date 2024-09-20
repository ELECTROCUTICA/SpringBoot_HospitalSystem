package com.HospitalSystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@SpringBootConfiguration
@MapperScan("com.HospitalSystem.Mapper")
public class SpringBootHospitalSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootHospitalSystemApplication.class, args);
    }

}
