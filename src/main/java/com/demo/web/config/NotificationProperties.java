package com.demo.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "notification.mail")
public class NotificationProperties {

    /**
     * Bật/tắt gửi email thật. Khi false hệ thống chỉ ghi log.
     */
    private boolean enabled = false;

    /**
     * Email nhận thông báo booking.
     */
    private String bookingRecipient = "booking@wedding.local";

    /**
     * Email nhận thông báo contact.
     */
    private String contactRecipient = "support@wedding.local";

    /**
     * Địa chỉ email hiển thị trong phần gửi.
     */
    private String from = "no-reply@wedding.local";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getBookingRecipient() {
        return bookingRecipient;
    }

    public void setBookingRecipient(String bookingRecipient) {
        this.bookingRecipient = bookingRecipient;
    }

    public String getContactRecipient() {
        return contactRecipient;
    }

    public void setContactRecipient(String contactRecipient) {
        this.contactRecipient = contactRecipient;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
