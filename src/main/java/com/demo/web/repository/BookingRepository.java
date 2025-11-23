package com.demo.web.repository;

import com.demo.web.model.Booking;
import com.demo.web.model.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByStatus(BookingStatus status);

    List<Booking> findByEventDateBetween(LocalDate start, LocalDate end);

    boolean existsByHallIdAndEventDateAndTimeSlotAndStatusIn(Long hallId,
                                                             LocalDate eventDate,
                                                             String timeSlot,
                                                             Collection<BookingStatus> statuses);

    boolean existsByEmailIgnoreCaseAndEventDate(String email, LocalDate eventDate);

    long countByStatus(BookingStatus status);

    List<Booking> findTop5ByEventDateGreaterThanEqualOrderByEventDateAsc(LocalDate date);
}
