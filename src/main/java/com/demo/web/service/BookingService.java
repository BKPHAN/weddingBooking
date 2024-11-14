package com.demo.web.service;

import com.demo.web.model.Booking;
import com.demo.web.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    @Autowired
    private BookingRepository weddingRepository;

    public boolean saveBooking(Booking booking) {
        try {
            weddingRepository.save(booking);
            return true;
        } catch (Exception e) {
            // Xử lý lỗi khi lưu thông tin đám cưới, ví dụ: ghi log hoặc ném ngoại lệ tuỳ chỉnh
            e.printStackTrace();
            return false;
        }
    }
}