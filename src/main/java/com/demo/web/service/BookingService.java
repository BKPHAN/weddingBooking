package com.demo.web.service;

import com.demo.web.exception.BookingConflictException;
import com.demo.web.exception.InvalidRequestException;
import com.demo.web.model.Booking;
import com.demo.web.model.Hall;
import com.demo.web.model.enums.BookingStatus;
import com.demo.web.repository.BookingRepository;
import com.demo.web.repository.HallRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final HallRepository hallRepository;
    private final NotificationService notificationService;

    public BookingService(BookingRepository bookingRepository,
                          HallRepository hallRepository,
                          NotificationService notificationService) {
        this.bookingRepository = bookingRepository;
        this.hallRepository = hallRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public Booking createBooking(Booking booking, Long hallId, Set<String> services) {
        validateBookingRequest(booking);

        if (bookingRepository.existsByEmailIgnoreCaseAndEventDate(booking.getEmail(), booking.getEventDate())) {
            throw new InvalidRequestException("Bạn đã gửi yêu cầu đặt tiệc cho ngày này. Vui lòng chờ phản hồi hoặc liên hệ trực tiếp.");
        }

        if (hallId != null) {
            Hall hall = hallRepository.findById(hallId)
                    .orElseThrow(() -> new EntityNotFoundException("Hall not found with id: " + hallId));
            booking.setHall(hall);

            boolean hallTaken = bookingRepository.existsByHallIdAndEventDateAndTimeSlotAndStatusIn(
                    hall.getId(),
                    booking.getEventDate(),
                    booking.getTimeSlot(),
                    EnumSet.of(BookingStatus.PENDING, BookingStatus.CONFIRMED)
            );

            if (hallTaken) {
                throw new BookingConflictException("Khung giờ này của sảnh đã có khách khác đặt. Vui lòng chọn sảnh hoặc thời gian khác.");
            }
        }

        if (services != null) {
            booking.setServices(new HashSet<>(services));
        }

        booking.setStatus(Optional.ofNullable(booking.getStatus()).orElse(BookingStatus.PENDING));
        booking.setFlag(Optional.ofNullable(booking.getFlag()).orElse(booking.getStatus().name()));
        Booking saved = bookingRepository.save(booking);
        notificationService.notifyBookingReceived(saved);
        return saved;
    }

    @Transactional(readOnly = true)
    public List<Booking> getBookingsByStatus(BookingStatus status) {
        return bookingRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<Booking> getBookingsBetween(LocalDate from, LocalDate to) {
        return bookingRepository.findByEventDateBetween(from, to);
    }

    private void validateBookingRequest(Booking booking) {
        if (booking.getEventDate() == null) {
            throw new InvalidRequestException("Vui lòng chọn ngày tổ chức hợp lệ.");
        }
        if (booking.getEventDate().isBefore(LocalDate.now())) {
            throw new InvalidRequestException("Ngày tổ chức phải lớn hơn hoặc bằng ngày hôm nay.");
        }

        if (booking.getGuestCount() == null || booking.getGuestCount() <= 0) {
            throw new InvalidRequestException("Số lượng khách mời phải lớn hơn 0.");
        }

        if (booking.getBudgetMin() != null && booking.getBudgetMin().signum() < 0) {
            throw new InvalidRequestException("Ngân sách tối thiểu không được âm.");
        }

        if (booking.getBudgetMax() != null && booking.getBudgetMax().signum() < 0) {
            throw new InvalidRequestException("Ngân sách tối đa không được âm.");
        }

        if (booking.getBudgetMin() != null && booking.getBudgetMax() != null
                && booking.getBudgetMin().compareTo(booking.getBudgetMax()) > 0) {
            throw new InvalidRequestException("Ngân sách tối thiểu không được lớn hơn ngân sách tối đa.");
        }
    }
}
