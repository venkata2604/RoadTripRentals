package com.carrental.serviceImpl;

import com.carrental.dto.CarDto;
import com.carrental.entity.BranchEntity;
import com.carrental.entity.BranchManagerEntity;
import com.carrental.entity.CarEntity;
import com.carrental.entity.CarOwnerEntity;
import com.carrental.exception.CarNotFoundException;
import com.carrental.repository.BranchManagerRepository;
import com.carrental.repository.BranchRepository;
import com.carrental.repository.CarOwnerRepository;
import com.carrental.repository.CarRepository;
import com.carrental.service.CarService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl implements CarService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private BranchManagerRepository branchManagerRepository;

    @Autowired
    private CarOwnerRepository carOwnerRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${app.cars.images-folder-path}")
    private String imagesFolderPath;

    @Override
    public List<CarDto> getCars() {
        List<CarEntity> cars = carRepository.findAll();
        return cars.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public CarDto getCar(Long id) throws CarNotFoundException {
        Optional<CarEntity> carEntity = carRepository.findById(id);
        if (carEntity.isEmpty()) {
            throw new CarNotFoundException("Car not found");
        } else {
            return mapToDto(carEntity.get());
        }
    }

    @Override
    public CarDto addCar(CarDto carDto) {
        // Fetch the maximum car ID
        Long maxCarId = carRepository.findMaxCarId();
        long newCarId = maxCarId + 1;

        if (carDto.getImage() != null && !carDto.getImage().isEmpty()) {
            String imageFileName = handleFileUpload(newCarId, carDto.getImage());
            carDto.setImageUrl(imageFileName);
        }

        CarEntity carEntity = modelMapper.map(carDto, CarEntity.class);
        CarEntity savedCarEntity = carRepository.save(carEntity);
        return mapToDto(savedCarEntity);
    }


    @Override
    public CarDto updateCar(Long id, CarDto updatedCarDto) throws CarNotFoundException {
        Optional<CarEntity> existingCarEntity = carRepository.findById(id);
        if (existingCarEntity.isEmpty()) throw new CarNotFoundException("Car not found");

        CarEntity savedCarEntity = existingCarEntity.get();

        if (updatedCarDto.getImage() != null && !updatedCarDto.getImage().isEmpty()) {
            String imageFileName = handleFileUpload(id, updatedCarDto.getImage());
            updatedCarDto.setImageUrl(imageFileName);
            savedCarEntity.setImageUrl(imageFileName);
        }

        savedCarEntity.setBrand(updatedCarDto.getBrand());
        savedCarEntity.setModel(updatedCarDto.getModel());
        savedCarEntity.setYear(updatedCarDto.getYear());
        savedCarEntity.setRentalRate(updatedCarDto.getRentalRate());
        savedCarEntity.setDescription(updatedCarDto.getDescription());
        savedCarEntity.setStatus(updatedCarDto.getStatus());

        if(updatedCarDto.getOwnerId() != 0){
            if(updatedCarDto.getStatus().equals("Approved")){
                savedCarEntity.setApprovedDate(LocalDate.now());
            }
            if(updatedCarDto.getStatus().equals("Returned")){
                savedCarEntity.setEndDate(LocalDate.now());
                long days = ChronoUnit.DAYS.between(savedCarEntity.getApprovedDate(), LocalDate.now());
                double perDayRent = updatedCarDto.getProposedOwnerRent() / 30; // Assuming 30 days in a month
                double totalAmount = days * perDayRent;
                savedCarEntity.setOwnerBalance(totalAmount);
            }
        }

        savedCarEntity.setCarId(id);

        carRepository.save(savedCarEntity);
        return mapToDto(savedCarEntity);
    }

    @Override
    public void deleteCar(Long id) throws CarNotFoundException {
        Optional<CarEntity> existingCarEntity = carRepository.findById(id);
        if (existingCarEntity.isEmpty()) throw new CarNotFoundException("Car not found");

        carRepository.deleteById(id);
    }

    @Override
    public List<CarDto> getCarsByBranch(Long branchId) {
        List<CarEntity> cars = carRepository.findAllByBranchId(branchId);
        return cars.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<CarDto> getCarsByOwner(Long ownerId) {
        List<CarEntity> cars = carRepository.findAllByOwnerId(ownerId);
        return cars.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private CarDto mapToDto(CarEntity entity){
        CarDto carDto = modelMapper.map(entity, CarDto.class);
        Optional<BranchEntity> branchEntity = branchRepository.findById(entity.getBranchId());
        if(branchEntity.isPresent()){
            carDto.setBranchName(branchEntity.get().getBranchName());
            carDto.setBranchLocation(branchEntity.get().getLocation());
            carDto.setBranchId(branchEntity.get().getBranchId());
            Optional<BranchManagerEntity> branchManagerEntity = branchManagerRepository
                    .findById(branchEntity.get().getBranchManagerId());
            branchManagerEntity.ifPresent(managerEntity -> carDto.setBranchManagerName(managerEntity.getName()));
        }

        if(entity.getOwnerId() != null && entity.getOwnerId() != 0){
            Optional<CarOwnerEntity> carOwnerEntity = carOwnerRepository.findById(entity.getOwnerId());
            if (carOwnerEntity.isPresent()) {
                CarOwnerEntity carOwner = carOwnerEntity.get();
                carDto.setOwnerName(carOwner.getFirstName()+" "+carOwner.getLastName());
            }
        }

        return carDto;
    }

    private String handleFileUpload(Long carId, MultipartFile carImage) {
        String uniqueFileName = "";
        try {
            String originalFileName = carImage.getOriginalFilename();
            assert originalFileName != null;
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            uniqueFileName = "car_"+carId + fileExtension;

            Path filePath = Paths.get(imagesFolderPath, uniqueFileName);

            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }

            Files.write(filePath, carImage.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uniqueFileName;
    }
}
