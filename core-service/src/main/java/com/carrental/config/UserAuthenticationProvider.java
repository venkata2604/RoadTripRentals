package com.carrental.config;

import com.carrental.dto.AdminDto;
import com.carrental.dto.UserDto;
import com.carrental.dto.BranchManagerDto;
import com.carrental.dto.CarOwnerDto;
import com.carrental.exception.AdminNotFoundException;
import com.carrental.exception.UserNotFoundException;
import com.carrental.exception.BranchManagerNotFoundException;
import com.carrental.exception.CarOwnerNotFoundException;
import com.carrental.service.AdminService;
import com.carrental.service.UserService;
import com.carrental.service.BranchManagerService;
import com.carrental.service.CarOwnerService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.*;

@Component
public class UserAuthenticationProvider {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private BranchManagerService branchManagerService;

    @Autowired
    private CarOwnerService carOwnerService;

    @Value("${jwt.secret}")
    private String secretKey;

    @PostConstruct
    protected void init() {
        // this is to avoid having the raw secret key available in the JVM
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String login, Long userId, String role) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + 24 * 3600000); // 1 hour

        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        return JWT.create()
                .withSubject(login)
                .withClaim("userId", userId)
                .withClaim("role", role)
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .sign(algorithm);
    }

    public Authentication validateToken(String token) throws AdminNotFoundException, UserNotFoundException, BranchManagerNotFoundException, CarOwnerNotFoundException {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        AdminDto adminDto = null;
        UserDto userDto = null;
        BranchManagerDto branchManagerDto = null;
        CarOwnerDto carOwnerDto = null;

        JWTVerifier verifier = JWT.require(algorithm)
                .build();

        DecodedJWT decoded = verifier.verify(token);
        String role = decoded.getClaim("role").asString();
        List<GrantedAuthority> authorities = new ArrayList<>();

        switch (role) {
            case "ROLE_ADMIN" -> {
                adminDto = adminService.findByEmail(decoded.getSubject());
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                adminDto.setAuthorities(authorities);
                adminDto.setUsername(decoded.getSubject());
                return new UsernamePasswordAuthenticationToken(adminDto, null, authorities);
            }
            case "ROLE_USER" -> {
                userDto = userService.findByEmail(decoded.getSubject());
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                userDto.setAuthorities(authorities);
                userDto.setUsername(decoded.getSubject());
                return new UsernamePasswordAuthenticationToken(userDto, null, authorities);
            }
            case "ROLE_BRANCH_MANAGER" -> {
                branchManagerDto = branchManagerService.findByEmail(decoded.getSubject());
                authorities.add(new SimpleGrantedAuthority("ROLE_BRANCH_MANAGER"));
                branchManagerDto.setAuthorities(authorities);
                branchManagerDto.setUsername(decoded.getSubject());
                return new UsernamePasswordAuthenticationToken(branchManagerDto, null, authorities);
            }
            case "ROLE_CAR_OWNER" -> {
                carOwnerDto = carOwnerService.findByEmail(decoded.getSubject());
                authorities.add(new SimpleGrantedAuthority("ROLE_CAR_OWNER"));
                carOwnerDto.setAuthorities(authorities);
                carOwnerDto.setUsername(decoded.getSubject());
                return new UsernamePasswordAuthenticationToken(carOwnerDto, null, authorities);
            }
            default -> {
                System.out.println("Unknown role: " + role);
                return null;
            }
        }
    }
}
