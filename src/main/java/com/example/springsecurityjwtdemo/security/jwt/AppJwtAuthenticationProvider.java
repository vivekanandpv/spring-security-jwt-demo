package com.example.springsecurityjwtdemo.security.jwt;

import com.example.springsecurityjwtdemo.services.AppUserDetailsService;
import com.example.springsecurityjwtdemo.services.IAppUserDetailsService;
import com.example.springsecurityjwtdemo.utils.AppJwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AppJwtAuthenticationProvider implements AuthenticationProvider {
    private final IAppUserDetailsService userDetailsService;
    private final AppJwtUtils jwtUtils;

    public AppJwtAuthenticationProvider(IAppUserDetailsService userDetailsService, AppJwtUtils jwtUtils) {
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String token = String.valueOf(authentication.getCredentials());

        try {
            String username = jwtUtils.extractUsername(token);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtUtils.validateToken(token, userDetails)) {
                return new AppJwtAuthentication(username, userDetails.getAuthorities());
            }
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException exception) {
            throw new BadCredentialsException("Token invalid");
        }

        // There is an edge case where token is valid, but user might be blocked
        throw new BadCredentialsException("Login failed");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AppJwtAuthentication.class.equals(authentication);
    }
}
