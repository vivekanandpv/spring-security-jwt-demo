package com.example.springsecurityjwtdemo.security.credentials_based;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AppUsernameAndPasswordFilter extends OncePerRequestFilter {
    private final AppUsernameAndPasswordAuthenticationManager usernameAndPasswordAuthenticationManager;

    public AppUsernameAndPasswordFilter(AppUsernameAndPasswordAuthenticationManager usernameAndPasswordAuthenticationManager) {
        this.usernameAndPasswordAuthenticationManager = usernameAndPasswordAuthenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String username = httpServletRequest.getHeader("X-App-Username");
        String password = httpServletRequest.getHeader("X-App-Password");

        try {
            Authentication authentication = new AppUsernameAndPasswordAuthentication(username, password);
            authentication = usernameAndPasswordAuthenticationManager.authenticate(authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (AuthenticationException ae) {
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getServletPath().equals("/api/v1/auth/login")
                || request.getServletPath().equals("/api/v1/auth/reset-password")
                || request.getServletPath().equals("/api/v1/auth/register");
    }
}
