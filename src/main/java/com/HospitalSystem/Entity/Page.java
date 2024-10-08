package com.HospitalSystem.Entity;

import lombok.*;
import org.springframework.stereotype.Component;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Page {
    private int start;
    private int size;
    private int current_page;
}
