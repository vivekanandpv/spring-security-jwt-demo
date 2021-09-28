package com.example.springsecurityjwtdemo.controllers;

import com.example.springsecurityjwtdemo.services.IAuthService;
import com.example.springsecurityjwtdemo.viewmodels.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/auth")
public class AuthController {
    private final IAuthService authService;


    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginViewModel viewModel) {
        return ResponseEntity.ok(authService.getToken(viewModel));
    }


    @PostMapping(value = "/register")
//    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<GeneralServerResponse> register(@RequestBody UserRegisterViewModel viewModel) {
        return ResponseEntity.ok(authService.register(viewModel));
    }

    @GetMapping(value = "/users")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<List<UserListViewModel>> getUsers() {
        return ResponseEntity.ok(authService.getAllUsers());
    }

    @GetMapping(value = "/users/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<UserListViewModel> getUser(@PathVariable int id) {
        return ResponseEntity.ok(authService.getUserById(id));
    }

    @PostMapping(value = "/reset-password")
    public ResponseEntity<GeneralServerResponse> resetPassword(@RequestBody PasswordResetViewModel viewModel) {
        return ResponseEntity.ok(authService.resetPassword(viewModel));
    }

    @PostMapping(value = "/change-password")
    public ResponseEntity<GeneralServerResponse> changePassword(@RequestBody PasswordChangeViewModel viewModel) {
        return ResponseEntity.ok(authService.changePassword(viewModel));
    }

    @PostMapping(value = "/admin-block/{username}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<GeneralServerResponse> adminBlockUser(@PathVariable String username) {
        return ResponseEntity.ok(authService.adminBlock(username));
    }

    @PostMapping(value = "/admin-unblock/{username}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<GeneralServerResponse> adminUnblockUser(@PathVariable String username) {
        return ResponseEntity.ok(authService.adminUnblock(username));
    }

    @PostMapping(value = "/login-unblock/{username}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<GeneralServerResponse> loginUnblockUser(@PathVariable String username) {
        return ResponseEntity.ok(authService.loginUnblock(username));
    }
}
