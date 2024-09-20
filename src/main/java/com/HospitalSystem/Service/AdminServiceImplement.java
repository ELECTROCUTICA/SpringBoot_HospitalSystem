package com.HospitalSystem.Service;

import com.HospitalSystem.Entity.Admin;
import com.HospitalSystem.Mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("adminService")
public class AdminServiceImplement implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    public AdminMapper getAdminMapper() {
        return this.adminMapper;
    }

    public void setAdminMapper(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    @Override
    public void insertAdmin(Admin admin) {
        adminMapper.insertAdmin(admin);
    }

    @Override
    public void updateAdmin(Admin admin) {
        adminMapper.updateAdmin(admin);
    }

    @Override
    public Admin getAdmin(String id) {
        return adminMapper.getAdmin(id);
    }

    @Override
    public List<Admin> getAllAdmins() {
        return adminMapper.getAllAdmins();
    }
}
