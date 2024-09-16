package com.carrental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchManagerDto {
    private Long managerId;
    private String name;
    private String username;
    private String email;
    private String password;
    private String phone;
    private Long assignedBranchId;
    private String assignedBranchName;
    private String token;
    private String role;
    private List<GrantedAuthority> authorities;
}
