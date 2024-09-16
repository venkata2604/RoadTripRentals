package com.carrental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarOwnerDto {
    private Long ownerId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String streetAddress;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String token;
    private String username;
    private String role;
    private List<GrantedAuthority> authorities;
}
