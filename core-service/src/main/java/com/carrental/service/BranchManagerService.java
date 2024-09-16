package com.carrental.service;

import com.carrental.dto.BranchManagerDto;
import com.carrental.exception.BranchManagerNotFoundException;

import java.util.List;

public interface BranchManagerService {

    List<BranchManagerDto> getBranchManagers();

    BranchManagerDto getBranchManager(Long id);

    BranchManagerDto addBranchManager(BranchManagerDto branchManagerDto);

    BranchManagerDto updateBranchManager(Long id, BranchManagerDto updatedBranchManagerDto);

    void deleteBranchManager(Long id);

    BranchManagerDto login(BranchManagerDto branchManagerDto);

    BranchManagerDto findByEmail(String email) throws BranchManagerNotFoundException;

}
