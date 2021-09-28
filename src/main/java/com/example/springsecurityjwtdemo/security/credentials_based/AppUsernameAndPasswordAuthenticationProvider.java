package com.example.springsecurityjwtdemo.security.credentials_based;

import com.example.springsecurityjwtdemo.services.AppUserDetailsService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AppUsernameAndPasswordAuthenticationProvider implements AuthenticationProvider {
    private final AppUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public AppUsernameAndPasswordAuthenticationProvider(AppUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(authentication.getName());

        if (this.userDetailsService.processLogin(userDetails.getUsername(), String.valueOf(authentication.getCredentials()))) {
            return new AppUsernameAndPasswordAuthentication(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        }

        throw new BadCredentialsException("Login failed");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AppUsernameAndPasswordAuthentication.class.equals(authentication);
    }
}
