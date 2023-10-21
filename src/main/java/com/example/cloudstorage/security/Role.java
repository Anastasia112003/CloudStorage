package com.example.cloudstorage.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum Role implements GrantedAuthority {
    ADMIN_ROLE("ADMIN_ROLE"), USER_ROLE("USER_ROLE");

    private final String value;

    @Override
    public String getAuthority() {
        return value;
    }
}
