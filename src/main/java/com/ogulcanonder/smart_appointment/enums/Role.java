package com.ogulcanonder.smart_appointment.enums;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_BUSINESS, ROLE_EMPLOYEE, ROLE_USER;

    @Override
    public @Nullable String getAuthority() {
        return name();
    }
}
