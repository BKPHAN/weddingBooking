package com.demo.web.service;

import com.demo.web.model.Booking;
import com.demo.web.model.Contact;

public interface NotificationService {

    void notifyBookingReceived(Booking booking);

    void notifyContactReceived(Contact contact);
}
