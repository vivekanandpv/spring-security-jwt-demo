package com.example.springsecurityjwtdemo.security.credentials_based;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class AppUsernameAndPasswordAuthenticationManager implements AuthenticationManager {
    private final AppUsernameAndPasswordAuthenticationProvider usernameAndPasswordAuthenticationProvider;

    public AppUsernameAndPasswordAuthenticationManager(AppUsernameAndPasswordAuthenticationProvider usernameAndPasswordAuthenticationProvider) {
        this.usernameAndPasswordAuthenticationProvider = usernameAndPasswordAuthenticationProvider;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return this.usernameAndPasswordAuthenticationProvider.authenticate(authentication);
    }
}
