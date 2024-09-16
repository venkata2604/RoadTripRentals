package com.carrental.serviceImpl;

import com.carrental.dto.CarOwnerDto;
import com.carrental.entity.CarOwnerEntity;
import com.carrental.exception.CarOwnerNotFoundException;
import com.carrental.repository.CarOwnerRepository;
import com.carrental.service.CarOwnerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarOwnerServiceImpl implements CarOwnerService {

    @Autowired
    private CarOwnerRepository carOwnerRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<CarOwnerDto> getCarOwners() {
        List<CarOwnerEntity> carOwners = carOwnerRepository.findAll();
        return carOwners.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CarOwnerDto getCarOwner(Long id) {
        Optional<CarOwnerEntity> carOwnerEntity = carOwnerRepository.findById(id);
        if (carOwnerEntity.isPresent()) {
            return mapToDto(carOwnerEntity.get());
        }
        throw new CarOwnerNotFoundException("Car owner not found");
    }

    @Override
    public CarOwnerDto addCarOwner(CarOwnerDto carOwnerDto) {
        CarOwnerEntity carOwnerEntity = modelMapper.map(carOwnerDto, CarOwnerEntity.class);
        CarOwnerEntity savedCarOwnerEntity = carOwnerRepository.save(carOwnerEntity);
        return mapToDto(savedCarOwnerEntity);
    }

    @Override
    public CarOwnerDto updateCarOwner(Long id, CarOwnerDto updatedCarOwnerDto) {
        Optional<CarOwnerEntity> existingCarOwnerEntity = carOwnerRepository.findById(id);
        if (existingCarOwnerEntity.isPresent()) {
            CarOwnerEntity carOwnerEntity = existingCarOwnerEntity.get();
            carOwnerEntity.setFirstName(updatedCarOwnerDto.getFirstName());
            carOwnerEntity.setLastName(updatedCarOwnerDto.getLastName());
            carOwnerEntity.setEmail(updatedCarOwnerDto.getEmail());
            if (updatedCarOwnerDto.getPassword() != null && !updatedCarOwnerDto.getPassword().isEmpty()) {
                carOwnerEntity.setPassword(passwordEncoder.encode(updatedCarOwnerDto.getPassword()));
            }
            // Assuming address fields are part of the CarOwnerEntity
            carOwnerEntity.setStreetAddress(updatedCarOwnerDto.getStreetAddress());
            carOwnerEntity.setCity(updatedCarOwnerDto.getCity());
            carOwnerEntity.setState(updatedCarOwnerDto.getState());
            carOwnerEntity.setPostalCode(updatedCarOwnerDto.getPostalCode());
            carOwnerEntity.setCountry(updatedCarOwnerDto.getCountry());
            CarOwnerEntity updatedCarOwnerEntity = carOwnerRepository.save(carOwnerEntity);
            return mapToDto(updatedCarOwnerEntity);
        } else {
            throw new CarOwnerNotFoundException("Car owner not found");
        }
    }


    @Override
    public void deleteCarOwner(Long id) {
        if (!carOwnerRepository.existsById(id)) {
            throw new CarOwnerNotFoundException("Car owner not found");
        }
        carOwnerRepository.deleteById(id);
    }

    @Override
    public CarOwnerDto login(CarOwnerDto carOwnerDto) {
        CarOwnerEntity carOwnerEntity = carOwnerRepository.findByEmail(carOwnerDto.getEmail())
                .orElseThrow(() -> new CarOwnerNotFoundException("Car owner not found"));

        if (carOwnerDto.getPassword() != null && carOwnerDto.getPassword().equals(carOwnerEntity.getPassword())) {
            return mapToDto(carOwnerEntity);
        }
        throw new CarOwnerNotFoundException("Invalid password");
    }

    @Override
    public CarOwnerDto findByEmail(String email) throws CarOwnerNotFoundException {
        Optional<CarOwnerEntity> carOwnerEntity = carOwnerRepository.findByEmail(email);
        if (carOwnerEntity.isPresent()) {
            return mapToDto(carOwnerEntity.get());
        } else {
            throw new CarOwnerNotFoundException("Car owner not found");
        }
    }

    private CarOwnerDto mapToDto(CarOwnerEntity carOwnerEntity) {
        CarOwnerDto carOwnerDto = modelMapper.map(carOwnerEntity, CarOwnerDto.class);
        carOwnerDto.setPassword(null);
        carOwnerDto.setRole("ROLE_CAR_OWNER");
        return carOwnerDto;
    }
}
