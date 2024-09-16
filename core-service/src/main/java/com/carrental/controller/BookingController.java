package com.carrental.controller;

import com.carrental.dto.BookingDto;
import com.carrental.exception.BookingNotFoundException;
import com.carrental.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping
    public ResponseEntity<List<BookingDto>> getBookings() {
        List<BookingDto> bookings = bookingService.getBookings();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingDto>> getUserBookings(@PathVariable Long userId) {
        List<BookingDto> bookings = bookingService.getUserBookings(userId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<BookingDto>> getBranchBookings(@PathVariable Long branchId) {
        List<BookingDto> bookings = bookingService.getBranchBookings(branchId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> getBooking(@PathVariable Long id) {
        BookingDto booking = bookingService.getBooking(id);
        return ResponseEntity.ok(booking);
    }

    @PostMapping
    public ResponseEntity<BookingDto> addBooking(@RequestBody BookingDto bookingDto) {
        BookingDto newBooking = bookingService.addBooking(bookingDto);
        return ResponseEntity.ok(newBooking);
    }

    @PostMapping("/{id}/availability")
    public ResponseEntity<BookingDto> checkCarAvailability(@RequestBody BookingDto bookingDto) {
        BookingDto newBooking = bookingService.checkCarAvailability(bookingDto);
        return ResponseEntity.ok(newBooking);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingDto> updateBooking(@PathVariable Long id, @RequestBody BookingDto updatedBookingDto) {
        BookingDto updatedBooking = bookingService.updateBooking(id, updatedBookingDto);
        return ResponseEntity.ok(updatedBooking);
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<BookingDto> returnCar(@PathVariable Long id, @RequestBody BookingDto updatedBookingDto) {
        BookingDto updatedBooking = bookingService.returnCar(id, updatedBookingDto);
        return ResponseEntity.ok(updatedBooking);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<BookingDto> cancelBooking(@PathVariable Long id, @RequestBody BookingDto updatedBookingDto) {
        BookingDto updatedBooking = bookingService.cancelBooking(id, updatedBookingDto);
        return ResponseEntity.ok(updatedBooking);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
}
