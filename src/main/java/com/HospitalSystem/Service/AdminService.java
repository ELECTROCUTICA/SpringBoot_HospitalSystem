package com.HospitalSystem.Service;

import com.HospitalSystem.Entity.Admin;

import java.util.List;

public interface AdminService {
    void insertAdmin(Admin admin);

    void updateAdmin(Admin admin);

    Admin getAdmin(String id);

    List<Admin> getAllAdmins();
}
