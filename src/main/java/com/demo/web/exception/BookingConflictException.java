package com.demo.web.exception;

public class BookingConflictException extends BusinessException {
    public BookingConflictException(String message) {
        super(message);
    }
}
