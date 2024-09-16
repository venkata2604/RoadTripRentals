package com.carrental.serviceImpl;

import com.carrental.dto.BookingDto;
import com.carrental.dto.BranchDto;
import com.carrental.dto.PaymentDto;
import com.carrental.entity.BookingEntity;
import com.carrental.entity.BranchEntity;
import com.carrental.entity.CarEntity;
import com.carrental.entity.UserEntity;
import com.carrental.exception.BaseException;
import com.carrental.exception.BookingNotFoundException;
import com.carrental.exception.CarNotFoundException;
import com.carrental.repository.BookingRepository;
import com.carrental.repository.BranchRepository;
import com.carrental.repository.CarRepository;
import com.carrental.repository.UserRepository;
import com.carrental.service.BookingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${payment-service-url}")
    private String paymentServiceUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private CarRepository carRepository;

    @Override
    public List<BookingDto> getBookings() {
        List<BookingEntity> bookings = bookingRepository.findAll();
        return bookings.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getUserBookings(Long userId) {
        List<BookingEntity> bookings = bookingRepository.findAllByUserId(userId);
        return bookings.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getBranchBookings(Long branchId) {
        List<BookingEntity> bookings = bookingRepository.findAllByPickupLocation(branchId);
        return bookings.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public BookingDto returnCar(Long id, BookingDto updatedBookingDto) {
        Optional<BookingEntity> existingBookingEntity = bookingRepository.findById(id);
        if (existingBookingEntity.isEmpty()) throw new BookingNotFoundException("Booking not found");

        BookingEntity booking= existingBookingEntity.get();

        if(updatedBookingDto.getFinalAmount() > 0) {
            booking.setFinalAmount(updatedBookingDto.getFinalAmount());
            booking.setTotalAmount(booking.getTotalAmount()+updatedBookingDto.getFinalAmount());

            PaymentDto paymentDto = new PaymentDto();
            paymentDto.setPaymentAmount(updatedBookingDto.getFinalAmount());
            paymentDto.setCardNumber(updatedBookingDto.getCardNumber());
            paymentDto.setExpiryDate(updatedBookingDto.getExpiryDate());
            paymentDto.setCvv(updatedBookingDto.getCvv());
            paymentDto.setBookingId(booking.getBookingId());
            paymentDto.setPaymentStatus("Success");
            paymentDto.setPaymentDate(LocalDateTime.now());

            String paymentUrl = paymentServiceUrl + "/payments";
            try {
                PaymentDto paymentResponse = restTemplate.postForObject(paymentUrl, paymentDto, PaymentDto.class);

                if (paymentResponse == null || !"Success".equals(paymentResponse.getPaymentStatus())) {
                    throw new BaseException("Payment failed");
                }

                booking.setBookingId(booking.getBookingId());
                booking.setBookingStatus("Closed");
                booking.setFinalAmountStatus("Paid");
                booking.setDropoffLocation(updatedBookingDto.getDropoffLocation());
                booking.setActualReturnDate(LocalDate.now());
                bookingRepository.save(booking);

            } catch (Exception e) {
                e.printStackTrace();
                throw new BaseException("An error occurred during the payment process");
            }

        }else{
            booking.setFinalAmount(0);
            booking.setFinalAmountStatus("Paid");
            booking.setBookingStatus("Closed");
            booking.setDropoffLocation(updatedBookingDto.getDropoffLocation());
            booking.setActualReturnDate(LocalDate.now());
            bookingRepository.save(booking);
        }
        return mapToDto(booking);
    }

    @Override
    public BookingDto cancelBooking(Long id, BookingDto updatedBookingDto) {
        Optional<BookingEntity> existingBookingEntity = bookingRepository.findById(id);
        if (existingBookingEntity.isEmpty()) throw new BookingNotFoundException("Booking not found");

        BookingEntity booking= existingBookingEntity.get();
        booking.setBookingStatus("Refunded");

        bookingRepository.save(booking);

        return mapToDto(booking);
    }

    @Override
    public BookingDto getBooking(Long id) throws BookingNotFoundException {
        Optional<BookingEntity> bookingEntity = bookingRepository.findById(id);
        if (bookingEntity.isEmpty()) {
            throw new BookingNotFoundException("Booking not found");
        } else {
            return mapToDto(bookingEntity.get());
        }
    }

    @Transactional
    @Override
    public BookingDto addBooking(BookingDto bookingDto) {
        // Step 1: Retrieve the maximum booking ID
        Long maxBookingId = bookingRepository.findMaxBookingId();
        Long newBookingId = (maxBookingId != null) ? maxBookingId + 1 : 1L;

        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setPaymentAmount(bookingDto.getInitialAmount());
        paymentDto.setCardNumber(bookingDto.getCardNumber());
        paymentDto.setExpiryDate(bookingDto.getExpiryDate());
        paymentDto.setCvv(bookingDto.getCvv());
        paymentDto.setBookingId(newBookingId);
        paymentDto.setPaymentStatus("Success");
        paymentDto.setPaymentDate(LocalDateTime.now());

        String paymentUrl = paymentServiceUrl + "/payments";
        try {
            PaymentDto paymentResponse = restTemplate.postForObject(paymentUrl, paymentDto, PaymentDto.class);

            if (paymentResponse == null || !"Success".equals(paymentResponse.getPaymentStatus())) {
                throw new BaseException("Payment failed");
            }

            BookingEntity bookingEntity = modelMapper.map(bookingDto, BookingEntity.class);
            bookingEntity.setBookingId(newBookingId);
            bookingEntity.setBookingStatus("Confirmed");
            BookingEntity savedBookingEntity = bookingRepository.save(bookingEntity);

            return mapToDto(savedBookingEntity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException("An error occurred during the payment process");
        }
    }

    @Override
    public BookingDto updateBooking(Long id, BookingDto updatedBookingDto) throws BookingNotFoundException {
        Optional<BookingEntity> existingBookingEntity = bookingRepository.findById(id);
        if (existingBookingEntity.isEmpty()) throw new BookingNotFoundException("Booking not found");

        BookingEntity updatedBookingEntity = modelMapper.map(updatedBookingDto, BookingEntity.class);
        updatedBookingEntity.setBookingId(id);
        bookingRepository.save(updatedBookingEntity);

        return mapToDto(updatedBookingEntity);
    }

    @Override
    public void deleteBooking(Long id) throws BookingNotFoundException {
        Optional<BookingEntity> existingBookingEntity = bookingRepository.findById(id);
        if (existingBookingEntity.isEmpty()) throw new BookingNotFoundException("Booking not found");

        bookingRepository.deleteById(id);
    }

    @Override
    public BookingDto checkCarAvailability(BookingDto bookingDto) {
        Long carId = bookingDto.getCarId();
        LocalDate pickupDate = bookingDto.getPickupDate();
        LocalDate dropoffDate = bookingDto.getDropoffDate();

        List<BookingEntity> conflictingBookings = bookingRepository.findConflictingBookings(carId, pickupDate, dropoffDate,"Confirmed");

        if (conflictingBookings.isEmpty()) {
            bookingDto.setCarAvailabilityStatus("available");
        } else {
            bookingDto.setCarAvailabilityStatus("not available");
        }
        return bookingDto;
    }

    private BookingDto mapToDto(BookingEntity bookingEntity){

        BookingDto bookingDto =  modelMapper.map(bookingEntity, BookingDto.class);
        Optional<UserEntity> userEntity = userRepository.findById(bookingEntity.getUserId());

        if(userEntity.isPresent()) {
            UserEntity user = userEntity.get();
            bookingDto.setUserName(user.getFirstName() +" "+ user.getLastName());
            bookingDto.setUserEmail(user.getEmail());
        }

        List<BranchEntity> branches = branchRepository.findAll();

        branches.forEach(branchEntity -> {
            if(Objects.equals(branchEntity.getBranchId(), bookingDto.getPickupLocation())){
                bookingDto.setPickupLocationName(branchEntity.getBranchName()+", "+branchEntity.getLocation());
            }
            if(Objects.equals(branchEntity.getBranchId(), bookingDto.getDropoffLocation())){
                bookingDto.setDropoffLocationName(branchEntity.getBranchName()+", "+branchEntity.getLocation());
            }
        });

        Optional<CarEntity> carEntity = carRepository.findById(bookingEntity.getCarId());
        if (carEntity.isPresent()) {
            CarEntity car = carEntity.get();
            bookingDto.setCarName(car.getBrand()+", "+car.getModel()+", "+car.getYear());
            bookingDto.setCarRentPerDay(car.getRentalRate());
        }

        return  bookingDto;
    }




}
