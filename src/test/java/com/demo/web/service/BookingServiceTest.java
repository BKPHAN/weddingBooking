package com.demo.web.service;

import com.demo.web.exception.BookingConflictException;
import com.demo.web.exception.InvalidRequestException;
import com.demo.web.model.Booking;
import com.demo.web.model.enums.BookingStatus;
import com.demo.web.repository.BookingRepository;
import com.demo.web.repository.HallRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private HallRepository hallRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private BookingService bookingService;

    private Booking booking;

    @BeforeEach
    void setUp() {
        booking = new Booking();
        booking.setBrideName("Lan");
        booking.setGroomName("Minh");
        booking.setEmail("lan@example.com");
        booking.setPhone("0123456789");
        booking.setEventDate(LocalDate.now().plusDays(10));
        booking.setTimeSlot("EVENING");
        booking.setGuestCount(200);
        booking.setBudgetMin(BigDecimal.valueOf(100_000_000));
        booking.setBudgetMax(BigDecimal.valueOf(150_000_000));
    }

    @Test
    void createBooking_shouldPersistAndNotify_whenRequestValid() {
        when(bookingRepository.existsByEmailIgnoreCaseAndEventDate(anyString(), any())).thenReturn(false);
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Booking saved = bookingService.createBooking(booking, null, Set.of("PHOTOGRAPHY", "LIVE_MUSIC"));

        ArgumentCaptor<Booking> bookingCaptor = ArgumentCaptor.forClass(Booking.class);
        verify(bookingRepository).save(bookingCaptor.capture());
        verify(notificationService).notifyBookingReceived(any(Booking.class));

        assertThat(saved.getStatus()).isEqualTo(BookingStatus.PENDING);
        assertThat(saved.getFlag()).isEqualTo(BookingStatus.PENDING.name());
        assertThat(saved.getServices()).containsExactlyInAnyOrder("PHOTOGRAPHY", "LIVE_MUSIC");
        assertThat(bookingCaptor.getValue().getEmail()).isEqualTo("lan@example.com");
    }

    @Test
    void createBooking_shouldThrow_whenDuplicateEmailAndDate() {
        when(bookingRepository.existsByEmailIgnoreCaseAndEventDate(eq("lan@example.com"), any()))
                .thenReturn(true);

        assertThatThrownBy(() -> bookingService.createBooking(booking, null, Set.of()))
                .isInstanceOf(InvalidRequestException.class);

        verify(bookingRepository, never()).save(any());
        verify(notificationService, never()).notifyBookingReceived(any());
    }

    @Test
    void createBooking_shouldValidateHallAvailability() {
        var hall = new com.demo.web.model.Hall();
        ReflectionTestUtils.setField(hall, "id", 1L);
        when(hallRepository.findById(1L)).thenReturn(Optional.of(hall));
        when(bookingRepository.existsByEmailIgnoreCaseAndEventDate(anyString(), any())).thenReturn(false);
        when(bookingRepository.existsByHallIdAndEventDateAndTimeSlotAndStatusIn(eq(1L), any(), anyString(), any()))
                .thenReturn(true);

        assertThatThrownBy(() -> bookingService.createBooking(booking, 1L, Set.of()))
                .isInstanceOf(BookingConflictException.class);

        verify(bookingRepository, never()).save(any());
        verify(notificationService, never()).notifyBookingReceived(any());
    }

    @Test
    void createBooking_shouldRejectInvalidBudgets() {
        booking.setBudgetMin(BigDecimal.valueOf(200));
        booking.setBudgetMax(BigDecimal.valueOf(100));

        assertThatThrownBy(() -> bookingService.createBooking(booking, null, Set.of()))
                .isInstanceOf(InvalidRequestException.class);

        booking.setBudgetMin(BigDecimal.valueOf(-10));
        booking.setBudgetMax(BigDecimal.valueOf(1000));

        assertThatThrownBy(() -> bookingService.createBooking(booking, null, Set.of()))
                .isInstanceOf(InvalidRequestException.class);
    }

    @Test
    void createBooking_shouldRejectPastDate() {
        booking.setEventDate(LocalDate.now().minusDays(1));

        assertThatThrownBy(() -> bookingService.createBooking(booking, null, Set.of()))
                .isInstanceOf(InvalidRequestException.class);
    }
}
