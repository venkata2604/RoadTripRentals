package com.carrental.serviceImpl;

import com.carrental.dto.BranchManagerDto;
import com.carrental.entity.BranchEntity;
import com.carrental.entity.BranchManagerEntity;
import com.carrental.exception.BranchManagerNotFoundException;
import com.carrental.repository.BranchManagerRepository;
import com.carrental.repository.BranchRepository;
import com.carrental.service.BranchManagerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BranchManagerServiceImpl implements BranchManagerService {

    @Autowired
    private BranchManagerRepository branchManagerRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<BranchManagerDto> getBranchManagers() {
        List<BranchManagerEntity> branchManagers = branchManagerRepository.findAll();
        return branchManagers.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public BranchManagerDto getBranchManager(Long id) {
        Optional<BranchManagerEntity> branchManagerEntity = branchManagerRepository.findById(id);
        if (branchManagerEntity.isPresent()) {
            return mapToDto(branchManagerEntity.get());
        }
        throw new BranchManagerNotFoundException("Branch manager not found");
    }


    @Override
    public BranchManagerDto addBranchManager(BranchManagerDto branchManagerDto) {
        BranchManagerEntity branchManagerEntity = modelMapper.map(branchManagerDto, BranchManagerEntity.class);
        BranchManagerEntity savedBranchManagerEntity = branchManagerRepository.save(branchManagerEntity);
        return mapToDto(savedBranchManagerEntity);
    }

    @Override
    public BranchManagerDto updateBranchManager(Long id, BranchManagerDto updatedBranchManagerDto) {
        Optional<BranchManagerEntity> existingBranchManagerEntity = branchManagerRepository.findById(id);
        if (existingBranchManagerEntity.isPresent()) {
            BranchManagerEntity updatedBranchManagerEntity = modelMapper.map(updatedBranchManagerDto, BranchManagerEntity.class);
            updatedBranchManagerEntity.setManagerId(id);
            branchManagerRepository.save(updatedBranchManagerEntity);
            return mapToDto(updatedBranchManagerEntity);
        } else {
            throw new BranchManagerNotFoundException("Branch manager not found");
        }
    }

    @Override
    public void deleteBranchManager(Long id) {
        branchManagerRepository.deleteById(id);
    }

    @Override
    public BranchManagerDto login(BranchManagerDto branchManagerDto) {
        BranchManagerEntity branchManagerEntity = branchManagerRepository.findByEmail(branchManagerDto.getEmail())
                .orElseThrow(() -> new BranchManagerNotFoundException("Branch manager not found"));

        if (branchManagerEntity.getPassword().equals(branchManagerDto.getPassword())) {
            return mapToDto(branchManagerEntity);
        }
        throw new BranchManagerNotFoundException("Invalid password");
    }


    @Override
    public BranchManagerDto findByEmail(String email) throws BranchManagerNotFoundException {
        Optional<BranchManagerEntity> branchManagerEntity = branchManagerRepository.findByEmail(email);
        if (branchManagerEntity.isPresent()) {
            return mapToDto(branchManagerEntity.get());
        } else {
            throw new BranchManagerNotFoundException("Branch manager not found");
        }
    }

    private BranchManagerDto mapToDto(BranchManagerEntity branchManagerEntity){

        BranchManagerDto branchManagerDto = modelMapper.map(branchManagerEntity, BranchManagerDto.class);
        branchManagerDto.setPassword(null);
        branchManagerDto.setRole("ROLE_BRANCH_MANAGER");

        Optional<BranchEntity> branchEntity = branchRepository.
                findByBranchManagerId(branchManagerEntity.getManagerId());

        branchEntity.ifPresent(entity -> {
            branchManagerDto.setAssignedBranchName(entity.getBranchName());
            branchManagerDto.setAssignedBranchId(entity.getBranchId());
        });

        return branchManagerDto;

    }

}
