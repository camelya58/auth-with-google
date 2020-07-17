package com.github.camelya58.auth_with_google.model;

import org.springframework.security.core.GrantedAuthority;

/**
 * Enum Role contains only one role - user.
 *
 * @author Kamila Meshcheryakova
 * created 17.07.2020
 */
public enum Role implements GrantedAuthority {
    USER;

    @Override
    public String getAuthority() {
        return name();
    }
}
