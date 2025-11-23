package com.demo.web.service.impl;

import com.demo.web.config.NotificationProperties;
import com.demo.web.model.Booking;
import com.demo.web.model.Contact;
import com.demo.web.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class MailNotificationService implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(MailNotificationService.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final JavaMailSender mailSender;
    private final NotificationProperties properties;

    public MailNotificationService(@Nullable JavaMailSender mailSender,
                                   NotificationProperties properties) {
        this.mailSender = mailSender;
        this.properties = properties;
    }

    @Override
    public void notifyBookingReceived(Booking booking) {
        log.info("Booking received for {} & {} on {} ({})",
                booking.getBrideName(),
                booking.getGroomName(),
                booking.getEventDate(),
                booking.getEmail());

        if (!properties.isEnabled() || mailSender == null) {
            log.debug("Mail notification disabled or mailSender not configured. Skipping email for booking.");
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(properties.getFrom());
        message.setTo(properties.getBookingRecipient());
        message.setSubject("[Wedding Booking] Yêu cầu đặt tiệc mới");
        message.setText("""
                Có yêu cầu đặt tiệc mới:

                Cô dâu: %s
                Chú rể: %s
                Email: %s
                Số điện thoại: %s
                Ngày tổ chức: %s (%s)
                Sảnh: %s
                Số khách: %s
                Dịch vụ: %s
                Ghi chú: %s
                """.formatted(
                booking.getBrideName(),
                booking.getGroomName(),
                booking.getEmail(),
                booking.getPhone(),
                Optional.ofNullable(booking.getEventDate()).map(DATE_FORMATTER::format).orElse("Chưa chọn"),
                booking.getTimeSlot(),
                booking.getHall() != null ? booking.getHall().getName() : "Chưa chọn",
                Optional.ofNullable(booking.getGuestCount()).map(Object::toString).orElse("Chưa rõ"),
                booking.getServices().isEmpty() ? "Không" : String.join(", ", booking.getServices()),
                Optional.ofNullable(booking.getNotes()).orElse("Không có")
        ));

        sendEmail(message);
    }

    @Override
    public void notifyContactReceived(Contact contact) {
        log.info("Contact received from {} ({}) flag={} subject={}",
                contact.getFullName(), contact.getEmail(), contact.getFlag(), contact.getSubject());

        if (!properties.isEnabled() || mailSender == null) {
            log.debug("Mail notification disabled or mailSender not configured. Skipping email for contact.");
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(properties.getFrom());
        message.setTo(properties.getContactRecipient());
        message.setSubject("[Wedding Contact] Khách hàng liên hệ mới");
        message.setText("""
                Có phản hồi mới từ khách hàng:

                Họ tên: %s
                Email: %s
                Số điện thoại: %s
                Chủ đề: %s
                Trạng thái: %s
                Nội dung:
                %s
                """.formatted(
                contact.getFullName(),
                contact.getEmail(),
                contact.getPhone(),
                Optional.ofNullable(contact.getSubject()).orElse("Không có"),
                Optional.ofNullable(contact.getFlag()).orElse("NEW"),
                contact.getMessage()
        ));

        sendEmail(message);
    }

    private void sendEmail(SimpleMailMessage message) {
        try {
            mailSender.send(message);
        } catch (MailException ex) {
            log.warn("Không thể gửi email thông báo: {}", ex.getMessage());
            log.debug("Chi tiết lỗi gửi mail", ex);
        }
    }
}
