package com.carrental.controller;

import com.carrental.dto.CarDto;
import com.carrental.exception.CarNotFoundException;
import com.carrental.service.CarService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarController {

    @Autowired
    private CarService carService;

    @GetMapping
    public ResponseEntity<List<CarDto>> getCars() {
        List<CarDto> cars = carService.getCars();
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<CarDto>> getCarsByBranch(@PathVariable Long branchId) {
        List<CarDto> cars = carService.getCarsByBranch(branchId);
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<CarDto>> getCarsByOwner(@PathVariable Long ownerId) {
        List<CarDto> cars = carService.getCarsByOwner(ownerId);
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarDto> getCar(@PathVariable Long id) {
        CarDto car = carService.getCar(id);
        return ResponseEntity.ok(car);
    }

    @PostMapping
    public ResponseEntity<CarDto> addCar(@RequestPart("image") MultipartFile image
            , @RequestPart("carDto") String carDtoJson) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        CarDto newCar = objectMapper.readValue(carDtoJson, CarDto.class);
        newCar.setImage(image);
        CarDto savedCar = carService.addCar(newCar);

        return ResponseEntity.ok(savedCar);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarDto> updateCar(@PathVariable Long id,
                                            @RequestPart(value = "image", required = false) MultipartFile image,
                                            @RequestPart("carDto") String carDtoJson) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        CarDto updatedCarDto = objectMapper.readValue(carDtoJson, CarDto.class);
        updatedCarDto.setCarId(id);
        if (image != null) {
            updatedCarDto.setImage(image);
        }
        CarDto updatedCar = carService.updateCar(id,updatedCarDto);
        return ResponseEntity.ok(updatedCar);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }
}
