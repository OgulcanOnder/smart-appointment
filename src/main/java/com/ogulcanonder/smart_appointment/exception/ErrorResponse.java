package com.ogulcanonder.smart_appointment.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        List<String> message,
        String path
) {
}
