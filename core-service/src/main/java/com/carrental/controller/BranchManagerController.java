package com.carrental.controller;

import com.carrental.config.UserAuthenticationProvider;
import com.carrental.dto.BranchManagerDto;
import com.carrental.service.BranchManagerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/branch-managers")
public class BranchManagerController {

    @Autowired
    private BranchManagerService branchManagerService;

    @Autowired
    private UserAuthenticationProvider userAuthenticationProvider;

    @PostMapping("/login")
    public ResponseEntity<BranchManagerDto> login(@Valid @RequestBody BranchManagerDto branchManagerDto) {
        BranchManagerDto branchManager = branchManagerService.login(branchManagerDto);
        if (branchManager != null) {
            String token = userAuthenticationProvider.createToken(branchManagerDto.getEmail(), branchManager.getManagerId(), "ROLE_BRANCH_MANAGER");
            branchManager.setToken(token);
            return ResponseEntity.ok(branchManager);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<BranchManagerDto>> getBranchManagers() {
        List<BranchManagerDto> branchManagers = branchManagerService.getBranchManagers();
        return ResponseEntity.ok(branchManagers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BranchManagerDto> getBranchManager(@PathVariable Long id) {
        BranchManagerDto branchManager = branchManagerService.getBranchManager(id);
        return ResponseEntity.ok(branchManager);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BranchManagerDto> updateBranchManager(@PathVariable Long id, @RequestBody BranchManagerDto updatedBranchManagerDto) {
        BranchManagerDto updatedBranchManager = branchManagerService.updateBranchManager(id, updatedBranchManagerDto);
        return ResponseEntity.ok(updatedBranchManager);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBranchManager(@PathVariable Long id) {
        branchManagerService.deleteBranchManager(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<BranchManagerDto> addBranchManager(@Valid @RequestBody BranchManagerDto branchManagerDto) {
        BranchManagerDto newBranchManager = branchManagerService.addBranchManager(branchManagerDto);
        return new ResponseEntity<>(newBranchManager, HttpStatus.CREATED);
    }
}
