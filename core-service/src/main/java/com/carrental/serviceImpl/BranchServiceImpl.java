package com.carrental.serviceImpl;

import com.carrental.dto.BranchDto;
import com.carrental.entity.BranchEntity;
import com.carrental.entity.BranchManagerEntity;
import com.carrental.exception.BranchNotFoundException;
import com.carrental.repository.BranchManagerRepository;
import com.carrental.repository.BranchRepository;
import com.carrental.service.BranchService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BranchServiceImpl implements BranchService {

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private BranchManagerRepository branchManagerRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<BranchDto> getBranches() {
        List<BranchEntity> branches = branchRepository.findAll();
        return branches.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public BranchDto getBranch(Long id) throws BranchNotFoundException {
        Optional<BranchEntity> branchEntity = branchRepository.findById(id);
        if (branchEntity.isEmpty()) {
            throw new BranchNotFoundException("Branch not found");
        } else {
            return mapToDto(branchEntity.get());
        }
    }

    @Override
    public BranchDto addBranch(BranchDto branchDto) {
        BranchEntity branchEntity = modelMapper.map(branchDto, BranchEntity.class);
        BranchEntity savedBranchEntity = branchRepository.save(branchEntity);
        return mapToDto(savedBranchEntity);
    }

    @Override
    public BranchDto updateBranch(Long id, BranchDto updatedBranchDto) throws BranchNotFoundException {
        Optional<BranchEntity> existingBranchEntity = branchRepository.findById(id);
        if (existingBranchEntity.isEmpty()) throw new BranchNotFoundException("Branch not found");

        BranchEntity updatedBranchEntity = modelMapper.map(updatedBranchDto, BranchEntity.class);
        updatedBranchEntity.setBranchId(id);
        branchRepository.save(updatedBranchEntity);

        return mapToDto(updatedBranchEntity);
    }

    @Override
    public void deleteBranch(Long id) throws BranchNotFoundException {
        Optional<BranchEntity> existingBranchEntity = branchRepository.findById(id);
        if (existingBranchEntity.isEmpty()) throw new BranchNotFoundException("Branch not found");
        branchRepository.deleteById(id);
    }

    private BranchDto mapToDto(BranchEntity branchEntity){

        BranchDto dto = modelMapper.map(branchEntity, BranchDto.class);
        Optional<BranchManagerEntity> branchManagerEntity = branchManagerRepository.findById(branchEntity.getBranchManagerId());
        branchManagerEntity.ifPresent(managerEntity -> dto.setBranchManagerName(managerEntity.getName()));
        return dto;
    }
}
