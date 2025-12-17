package com.demo.web.dto.admin;

import com.demo.web.dto.BookingResponse;
import com.demo.web.dto.ContactResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class DashboardResponse {

    private long totalBookings;
    private Map<String, Long> bookingByStatus;
    private BigDecimal expectedRevenue;
    private long pendingContacts;
    private List<ContactResponse> recentContacts;
    private List<BookingResponse> upcomingBookings;

    public long getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(long totalBookings) {
        this.totalBookings = totalBookings;
    }

    public Map<String, Long> getBookingByStatus() {
        return bookingByStatus;
    }

    public void setBookingByStatus(Map<String, Long> bookingByStatus) {
        this.bookingByStatus = bookingByStatus;
    }

    public BigDecimal getExpectedRevenue() {
        return expectedRevenue;
    }

    public void setExpectedRevenue(BigDecimal expectedRevenue) {
        this.expectedRevenue = expectedRevenue;
    }

    public long getPendingContacts() {
        return pendingContacts;
    }

    public void setPendingContacts(long pendingContacts) {
        this.pendingContacts = pendingContacts;
    }

    public List<ContactResponse> getRecentContacts() {
        return recentContacts;
    }

    public void setRecentContacts(List<ContactResponse> recentContacts) {
        this.recentContacts = recentContacts;
    }

    public List<BookingResponse> getUpcomingBookings() {
        return upcomingBookings;
    }

    public void setUpcomingBookings(List<BookingResponse> upcomingBookings) {
        this.upcomingBookings = upcomingBookings;
    }

    private Map<Integer, Long> monthlyBookings;

    public Map<Integer, Long> getMonthlyBookings() {
        return monthlyBookings;
    }

    public void setMonthlyBookings(Map<Integer, Long> monthlyBookings) {
        this.monthlyBookings = monthlyBookings;
    }
}
