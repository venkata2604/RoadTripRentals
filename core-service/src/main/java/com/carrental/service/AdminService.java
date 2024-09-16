package com.carrental.service;

import com.carrental.dto.AdminDto;
import com.carrental.exception.AdminNotFoundException;

import java.util.List;
import java.util.Optional;

public interface AdminService {
    List<AdminDto> getAdmins();

    AdminDto getAdmin(Long id); // Changed return type to AdminDto and removed throws AdminNotFoundException

    AdminDto addAdmin(AdminDto adminDto);

    AdminDto updateAdmin(Long id, AdminDto updatedAdminDto); // Changed return type to AdminDto and removed throws AdminNotFoundException

    void deleteAdmin(Long id); // Removed throws AdminNotFoundException

    AdminDto findByEmail(String email); // Removed throws AdminNotFoundException

    AdminDto login(AdminDto adminDto);
}
