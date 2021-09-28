package com.example.springsecurityjwtdemo.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/sample")
public class SampleController {
    @GetMapping("authenticated-route")
    public String getAuthenticatedRoute() {
        return "authenticated route ok";
    }

    @GetMapping("authorized-route")
    @PreAuthorize("hasAuthority('admin')")
    public String getAuthorizedRoute() {
        return "authorized route ok";
    }
}
