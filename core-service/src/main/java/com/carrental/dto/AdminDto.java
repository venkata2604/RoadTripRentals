package com.carrental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDto {
    private Long adminId;
    private String username;
    private String email;
    private String password;
    private String token;
    private String role;
    private List<GrantedAuthority> authorities;
}

