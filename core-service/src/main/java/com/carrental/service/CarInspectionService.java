package com.carrental.service;

import com.carrental.dto.CarInspectionDto;

import java.util.List;
import java.util.Optional;

public interface CarInspectionService {
    List<CarInspectionDto> getCarInspections();

    CarInspectionDto getCarInspection(Long id);

    CarInspectionDto addCarInspection(CarInspectionDto carInspectionDto);

    CarInspectionDto updateCarInspection(Long id, CarInspectionDto updatedCarInspectionDto);

    void deleteCarInspection(Long id);
}
