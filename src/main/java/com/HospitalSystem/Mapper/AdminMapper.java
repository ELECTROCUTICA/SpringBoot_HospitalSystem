package com.HospitalSystem.Mapper;

import com.HospitalSystem.Entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AdminMapper {

    void insertAdmin(Admin admin);

    void updateAdmin(Admin admin);

    void deleteAdmin(String id);

    Admin getAdmin(String id);

    List<Admin> getAllAdmins();
}
