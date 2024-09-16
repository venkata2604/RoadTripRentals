package com.carrental.service;

import com.carrental.dto.BranchDto;

import java.util.List;
import java.util.Optional;

public interface BranchService {
    List<BranchDto> getBranches();

    BranchDto getBranch(Long id);

    BranchDto addBranch(BranchDto branchDto);

    BranchDto updateBranch(Long id, BranchDto updatedBranchDto);

    void deleteBranch(Long id);
}
