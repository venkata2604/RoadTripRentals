package com.carrental.serviceImpl;

import com.carrental.dto.AdminDto;
import com.carrental.entity.AdminEntity;
import com.carrental.exception.AdminNotFoundException;
import com.carrental.repository.AdminRepository;
import com.carrental.service.AdminService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<AdminDto> getAdmins() {
        List<AdminEntity> admins = adminRepository.findAll();
        return admins.stream().map(adminEntity -> modelMapper.map(adminEntity, AdminDto.class)).collect(Collectors.toList());
    }

    @Override
    public AdminDto getAdmin(Long id) {
        Optional<AdminEntity> adminEntity = adminRepository.findById(id);
        if (adminEntity.isPresent()) {
            AdminDto adminDto = modelMapper.map(adminEntity.get(), AdminDto.class);
            adminDto.setPassword(null);
            adminDto.setRole("ROLE_ADMIN");
            return adminDto;
        }
        throw new AdminNotFoundException("Admin not found");
    }

    @Override
    public AdminDto addAdmin(AdminDto adminDto) {
        AdminEntity adminEntity = modelMapper.map(adminDto, AdminEntity.class);
        AdminEntity savedAdminEntity = adminRepository.save(adminEntity);
        return modelMapper.map(savedAdminEntity, AdminDto.class);
    }

    @Override
    public AdminDto updateAdmin(Long id, AdminDto updatedAdminDto) {
        Optional<AdminEntity> existingAdminEntity = adminRepository.findById(id);
        if (existingAdminEntity.isPresent()) {
            AdminEntity updatedAdminEntity = modelMapper.map(updatedAdminDto, AdminEntity.class);
            updatedAdminEntity.setAdminId(id);
            adminRepository.save(updatedAdminEntity);
            return modelMapper.map(updatedAdminEntity, AdminDto.class);
        } else {
            return null; // Handle admin not found scenario
        }
    }

    @Override
    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);
    }

    @Override
    public AdminDto findByEmail(String email) {
        Optional<AdminEntity> adminEntity = adminRepository.findByEmail(email);
        return adminEntity.isPresent() ? modelMapper.map(adminEntity, AdminDto.class) : null;
    }

    @Override
    public AdminDto login(AdminDto adminDto) {
        AdminEntity adminEntity = adminRepository.findByEmail(adminDto.getEmail())
                .orElseThrow(() -> new AdminNotFoundException("Admin not found"));

        if (adminEntity.getPassword().equals(adminDto.getPassword())) {
            AdminDto finalAdminDetails = modelMapper.map(adminEntity, AdminDto.class);
            finalAdminDetails.setPassword(null);
            finalAdminDetails.setRole("ROLE_ADMIN");
            return finalAdminDetails;
        }
        throw new AdminNotFoundException("Invalid password");
    }
}
