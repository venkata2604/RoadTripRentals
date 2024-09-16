package com.carrental.controller;

import com.carrental.dto.CarInspectionDto;
import com.carrental.exception.CarInspectionNotFoundException;
import com.carrental.service.CarInspectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/car-inspections")
public class CarInspectionController {

    @Autowired
    private CarInspectionService carInspectionService;

    @GetMapping
    public ResponseEntity<List<CarInspectionDto>> getCarInspections() {
        List<CarInspectionDto> carInspections = carInspectionService.getCarInspections();
        return ResponseEntity.ok(carInspections);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarInspectionDto> getCarInspection(@PathVariable Long id) {
        CarInspectionDto carInspection = carInspectionService.getCarInspection(id);
        return ResponseEntity.ok(carInspection);
    }

    @PostMapping
    public ResponseEntity<CarInspectionDto> addCarInspection(@RequestBody CarInspectionDto carInspectionDto) {
        CarInspectionDto newCarInspection = carInspectionService.addCarInspection(carInspectionDto);
        return ResponseEntity.ok(newCarInspection);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarInspectionDto> updateCarInspection(@PathVariable Long id, @RequestBody CarInspectionDto updatedCarInspectionDto) {
        CarInspectionDto updatedCarInspection = carInspectionService.updateCarInspection(id, updatedCarInspectionDto);
        return ResponseEntity.ok(updatedCarInspection);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCarInspection(@PathVariable Long id) {
        carInspectionService.deleteCarInspection(id);
        return ResponseEntity.noContent().build();
    }
}
