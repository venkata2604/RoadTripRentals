package com.carrental.service;

import com.carrental.dto.CarDto;

import java.util.List;
import java.util.Optional;

public interface CarService {
    List<CarDto> getCars();

    CarDto getCar(Long id);

    CarDto addCar(CarDto carDto);

    CarDto updateCar(Long id, CarDto updatedCarDto);

    void deleteCar(Long id);

    List<CarDto> getCarsByBranch(Long branchId);

    List<CarDto> getCarsByOwner(Long ownerId);
}
