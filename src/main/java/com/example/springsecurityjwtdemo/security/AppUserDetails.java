package com.example.springsecurityjwtdemo.security;

import com.example.springsecurityjwtdemo.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

public class AppUserDetails implements UserDetails {
    private final User user;

    public AppUserDetails(User user) {
        this.user = user;
    }

    private boolean isUserAllowed() {
        return !this.user.isAdminBlock() && !this.user.isLoginBlock();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(c -> (GrantedAuthority) () -> c.getRole())
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isUserAllowed();
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isUserAllowed();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.isUserAllowed();
    }

    @Override
    public boolean isEnabled() {
        return this.isUserAllowed();
    }
}
