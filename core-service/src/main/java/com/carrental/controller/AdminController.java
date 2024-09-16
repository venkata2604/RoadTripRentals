package com.carrental.controller;

import com.carrental.config.UserAuthenticationProvider;
import com.carrental.dto.AdminDto;
import com.carrental.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserAuthenticationProvider userAuthenticationProvider;

    @GetMapping
    public ResponseEntity<List<AdminDto>> getAdmins() {
        List<AdminDto> admins = adminService.getAdmins();
        return ResponseEntity.ok(admins);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminDto> getAdmin(@PathVariable Long id) {
        AdminDto admin = adminService.getAdmin(id);
        return ResponseEntity.ok(admin);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminDto> updateAdmin(@PathVariable Long id, @RequestBody AdminDto updatedAdminDto) {
        AdminDto updatedAdmin = adminService.updateAdmin(id, updatedAdminDto);
        return ResponseEntity.ok(updatedAdmin);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<AdminDto> addAdmin(@Valid @RequestBody AdminDto adminDto) {
        AdminDto newAdmin = adminService.addAdmin(adminDto);
        return new ResponseEntity<>(newAdmin, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AdminDto> login(@Valid @RequestBody AdminDto adminDto) {
        AdminDto admin = adminService.login(adminDto);
        String token = userAuthenticationProvider.createToken(adminDto.getEmail(), admin.getAdminId(), "ROLE_ADMIN");
        admin.setToken(token);
        return ResponseEntity.ok(admin);
    }
}
