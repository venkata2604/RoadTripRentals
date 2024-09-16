package com.carrental.controller;

import com.carrental.config.UserAuthenticationProvider;
import com.carrental.dto.CarOwnerDto;
import com.carrental.service.CarOwnerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/car-owners")
public class CarOwnerController {

    @Autowired
    private CarOwnerService carOwnerService;

    @Autowired
    private UserAuthenticationProvider userAuthenticationProvider;

    @PostMapping("/login")
    public ResponseEntity<CarOwnerDto> login(@Valid @RequestBody CarOwnerDto carOwnerDto) {
        CarOwnerDto carOwner = carOwnerService.login(carOwnerDto);
        if (carOwner != null) {
            String token = userAuthenticationProvider.createToken(carOwnerDto.getEmail(), carOwner.getOwnerId(), "ROLE_CAR_OWNER");
            carOwner.setToken(token);
            return ResponseEntity.ok(carOwner);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<CarOwnerDto>> getCarOwners() {
        List<CarOwnerDto> carOwners = carOwnerService.getCarOwners();
        return ResponseEntity.ok(carOwners);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarOwnerDto> getCarOwner(@PathVariable Long id) {
        CarOwnerDto carOwner = carOwnerService.getCarOwner(id);
        return ResponseEntity.ok(carOwner);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarOwnerDto> updateCarOwner(@PathVariable Long id, @RequestBody CarOwnerDto updatedCarOwnerDto) {
        CarOwnerDto updatedCarOwner = carOwnerService.updateCarOwner(id, updatedCarOwnerDto);
        return ResponseEntity.ok(updatedCarOwner);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCarOwner(@PathVariable Long id) {
        carOwnerService.deleteCarOwner(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<CarOwnerDto> addCarOwner(@Valid @RequestBody CarOwnerDto carOwnerDto) {
        CarOwnerDto newCarOwner = carOwnerService.addCarOwner(carOwnerDto);
        return new ResponseEntity<>(newCarOwner, HttpStatus.CREATED);
    }
}
