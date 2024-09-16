package com.carrental.controller;

import com.carrental.dto.BranchDto;
import com.carrental.exception.BranchNotFoundException;
import com.carrental.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/branches")
public class BranchController {

    @Autowired
    private BranchService branchService;

    @GetMapping
    public ResponseEntity<List<BranchDto>> getBranches() {
        List<BranchDto> branches = branchService.getBranches();
        return ResponseEntity.ok(branches);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BranchDto> getBranch(@PathVariable Long id) {
        BranchDto branch = branchService.getBranch(id);
        return ResponseEntity.ok(branch);
    }

    @PostMapping
    public ResponseEntity<BranchDto> addBranch(@RequestBody BranchDto branchDto) {
        BranchDto newBranch = branchService.addBranch(branchDto);
        return ResponseEntity.ok(newBranch);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BranchDto> updateBranch(@PathVariable Long id, @RequestBody BranchDto updatedBranchDto) {
        BranchDto updatedBranch = branchService.updateBranch(id, updatedBranchDto);
        return ResponseEntity.ok(updatedBranch);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBranch(@PathVariable Long id) {
        branchService.deleteBranch(id);
        return ResponseEntity.noContent().build();
    }
}
