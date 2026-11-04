package com.ogulcanonder.smart_appointment.exception;

public class MailSendException extends RuntimeException {
    public MailSendException(String email, Throwable cause) {
        super("Mail Send Exception:" + email, cause);
    }
}
