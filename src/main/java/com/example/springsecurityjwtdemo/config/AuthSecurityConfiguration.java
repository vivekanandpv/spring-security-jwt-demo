package com.example.springsecurityjwtdemo.config;

import com.example.springsecurityjwtdemo.security.jwt.AppJwtAuthenticationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AuthSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final AppJwtAuthenticationFilter jwtAuthenticationFilter;

    public AuthSecurityConfiguration(AppJwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();

        http
                .csrf()
                .disable();

        http.addFilterAt(this.jwtAuthenticationFilter, BasicAuthenticationFilter.class);
    }
}
