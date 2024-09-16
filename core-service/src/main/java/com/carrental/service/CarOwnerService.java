package com.carrental.service;

import com.carrental.dto.CarOwnerDto;
import com.carrental.exception.CarOwnerNotFoundException;

import java.util.List;

public interface CarOwnerService {

    List<CarOwnerDto> getCarOwners();

    CarOwnerDto getCarOwner(Long id);

    CarOwnerDto addCarOwner(CarOwnerDto carOwnerDto);

    CarOwnerDto updateCarOwner(Long id, CarOwnerDto updatedCarOwnerDto);

    void deleteCarOwner(Long id);

    CarOwnerDto login(CarOwnerDto carOwnerDto) throws CarOwnerNotFoundException;

    CarOwnerDto findByEmail(String email) throws CarOwnerNotFoundException;
}
