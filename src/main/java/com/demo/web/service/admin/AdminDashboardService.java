package com.demo.web.service.admin;

import com.demo.web.dto.admin.DashboardResponse;
import com.demo.web.mapper.BookingMapper;
import com.demo.web.mapper.ContactMapper;
import com.demo.web.model.Booking;
import com.demo.web.model.enums.BookingStatus;
import com.demo.web.repository.BookingRepository;
import com.demo.web.repository.ContactRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service
public class AdminDashboardService {

        private static final Set<String> PENDING_FLAGS = Set.of("NEW", "FOLLOW_UP", "URGENT", "ASSIGNED");

        private final BookingRepository bookingRepository;
        private final ContactRepository contactRepository;
        private final BookingMapper bookingMapper;
        private final ContactMapper contactMapper;

        public AdminDashboardService(BookingRepository bookingRepository,
                        ContactRepository contactRepository,
                        BookingMapper bookingMapper,
                        ContactMapper contactMapper) {
                this.bookingRepository = bookingRepository;
                this.contactRepository = contactRepository;
                this.bookingMapper = bookingMapper;
                this.contactMapper = contactMapper;
        }

        @Transactional(readOnly = true)
        public DashboardResponse getOverview() {
                DashboardResponse response = new DashboardResponse();

                response.setTotalBookings(bookingRepository.count());

                Map<String, Long> bookingByStatus = java.util.Arrays.stream(BookingStatus.values())
                                .collect(java.util.stream.Collectors.toMap(
                                                BookingStatus::name,
                                                bookingRepository::countByStatus,
                                                (a, b) -> a,
                                                java.util.LinkedHashMap::new));
                response.setBookingByStatus(bookingByStatus);

                response.setExpectedRevenue(calculateExpectedRevenue());
                response.setPendingContacts(contactRepository.countByFlagIn(PENDING_FLAGS));
                response.setRecentContacts(
                                contactMapper.toResponseList(contactRepository.findTop10ByOrderByCreatedAtDesc()));
                response.setUpcomingBookings(
                                bookingRepository
                                                .findTop5ByEventDateGreaterThanEqualOrderByEventDateAsc(LocalDate.now())
                                                .stream()
                                                .map(bookingMapper::toResponse)
                                                .toList());

                List<Object[]> monthlyStats = bookingRepository.countBookingsByYear(LocalDate.now().getYear());
                Map<Integer, Long> monthlyBookings = new java.util.HashMap<>();
                for (int i = 1; i <= 12; i++) {
                        monthlyBookings.put(i, 0L);
                }
                for (Object[] row : monthlyStats) {
                        monthlyBookings.put((Integer) row[0], (Long) row[1]);
                }
                response.setMonthlyBookings(monthlyBookings);

                return response;
        }

        private BigDecimal calculateExpectedRevenue() {
                List<Booking> confirmed = bookingRepository.findByStatus(BookingStatus.CONFIRMED);
                return confirmed.stream()
                                .map(Booking::getBudgetMax)
                                .filter(Objects::nonNull)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
}
