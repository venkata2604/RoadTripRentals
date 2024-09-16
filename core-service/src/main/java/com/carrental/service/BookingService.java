package com.carrental.service;

import com.carrental.dto.BookingDto;

import java.util.List;
import java.util.Optional;

public interface BookingService {
    List<BookingDto> getBookings();

    BookingDto getBooking(Long id);

    BookingDto addBooking(BookingDto bookingDto);

    BookingDto updateBooking(Long id, BookingDto updatedBookingDto);

    void deleteBooking(Long id);

    BookingDto checkCarAvailability(BookingDto bookingDto);

    List<BookingDto> getUserBookings(Long userId);

    BookingDto returnCar(Long id, BookingDto updatedBookingDto);

    BookingDto cancelBooking(Long id, BookingDto updatedBookingDto);

    List<BookingDto> getBranchBookings(Long branchId);
}
