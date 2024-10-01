package com.HospitalSystem.Utils;

import com.HospitalSystem.Entity.Department;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdminArrangementResponse {
    private Map<Integer, DateJSON> dates;
    private String now;
    private ArrayList<Department> departments;
}
