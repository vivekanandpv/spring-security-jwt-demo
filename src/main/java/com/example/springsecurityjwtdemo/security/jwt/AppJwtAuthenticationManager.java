package com.example.springsecurityjwtdemo.security.jwt;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class AppJwtAuthenticationManager implements AuthenticationManager {
    private final AppJwtAuthenticationProvider jwtAuthenticationProvider;

    public AppJwtAuthenticationManager(AppJwtAuthenticationProvider jwtAuthenticationProvider) {
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return this.jwtAuthenticationProvider.authenticate(authentication);
    }
}
