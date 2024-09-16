package com.carrental.repository;

import com.carrental.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    @Query("SELECT b FROM BookingEntity b WHERE b.carId = :carId " +
            "AND ((b.pickupDate >= :pickupDate AND b.pickupDate < :dropoffDate) " +
            "OR (b.dropoffDate > :pickupDate AND b.dropoffDate <= :dropoffDate)) " +
            "AND b.bookingStatus = :bookingStatus")
    List<BookingEntity> findConflictingBookings(@Param("carId") Long carId,
                                                @Param("pickupDate") LocalDate pickupDate,
                                                @Param("dropoffDate") LocalDate dropoffDate,
                                                @Param("bookingStatus") String bookingStatus);

    @Query("SELECT MAX(b.bookingId) FROM BookingEntity b")
    Long findMaxBookingId();

    List<BookingEntity> findAllByUserId(Long userId);

    List<BookingEntity> findAllByPickupLocation(Long branchId);
}
