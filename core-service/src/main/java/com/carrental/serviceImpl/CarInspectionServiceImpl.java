package com.carrental.serviceImpl;

import com.carrental.dto.CarInspectionDto;
import com.carrental.entity.CarInspectionEntity;
import com.carrental.exception.CarInspectionNotFoundException;
import com.carrental.repository.CarInspectionRepository;
import com.carrental.service.CarInspectionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarInspectionServiceImpl implements CarInspectionService {

    @Autowired
    private CarInspectionRepository carInspectionRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<CarInspectionDto> getCarInspections() {
        List<CarInspectionEntity> carInspections = carInspectionRepository.findAll();
        return carInspections.stream().map(carInspectionEntity -> modelMapper.map(carInspectionEntity, CarInspectionDto.class)).collect(Collectors.toList());
    }

    @Override
    public CarInspectionDto getCarInspection(Long id) throws CarInspectionNotFoundException {
        Optional<CarInspectionEntity> carInspectionEntity = carInspectionRepository.findById(id);
        if (carInspectionEntity.isEmpty()) {
            throw new CarInspectionNotFoundException("Car Inspection not found");
        } else {
            return modelMapper.map(carInspectionEntity.get(), CarInspectionDto.class);
        }
    }

    @Override
    public CarInspectionDto addCarInspection(CarInspectionDto carInspectionDto) {
        CarInspectionEntity carInspectionEntity = modelMapper.map(carInspectionDto, CarInspectionEntity.class);
        CarInspectionEntity savedCarInspectionEntity = carInspectionRepository.save(carInspectionEntity);
        return modelMapper.map(savedCarInspectionEntity, CarInspectionDto.class);
    }

    @Override
    public CarInspectionDto updateCarInspection(Long id, CarInspectionDto updatedCarInspectionDto) throws CarInspectionNotFoundException {
        Optional<CarInspectionEntity> existingCarInspectionEntity = carInspectionRepository.findById(id);
        if (existingCarInspectionEntity.isEmpty()) throw new CarInspectionNotFoundException("Car Inspection not found");

        CarInspectionEntity updatedCarInspectionEntity = modelMapper.map(updatedCarInspectionDto, CarInspectionEntity.class);
        updatedCarInspectionEntity.setInspectionId(id);
        carInspectionRepository.save(updatedCarInspectionEntity);

        return modelMapper.map(updatedCarInspectionEntity, CarInspectionDto.class);
    }

    @Override
    public void deleteCarInspection(Long id) throws CarInspectionNotFoundException {
        Optional<CarInspectionEntity> existingCarInspectionEntity = carInspectionRepository.findById(id);
        if (existingCarInspectionEntity.isEmpty()) throw new CarInspectionNotFoundException("Car Inspection not found");

        carInspectionRepository.deleteById(id);
    }
}

