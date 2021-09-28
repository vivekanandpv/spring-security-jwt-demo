package com.example.springsecurityjwtdemo.security.credentials_based;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

public class AppUsernameAndPasswordAuthentication implements Authentication {
    private String username;
    private String password;
    private boolean authenticated;
    private Collection<? extends GrantedAuthority> authorities;

    public AppUsernameAndPasswordAuthentication(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public AppUsernameAndPasswordAuthentication(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.setAuthenticated(true);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public Object getCredentials() {
        return this.password;
    }

    @Override
    public Object getDetails() {
        return this.username;
    }

    @Override
    public Object getPrincipal() {
        return this.username;
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return this.username;
    }
}
