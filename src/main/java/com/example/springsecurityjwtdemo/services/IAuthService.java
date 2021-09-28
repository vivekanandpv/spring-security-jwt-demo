package com.example.springsecurityjwtdemo.services;

import com.example.springsecurityjwtdemo.viewmodels.*;

import java.util.List;

public interface IAuthService {
    TokenResponse getToken(LoginViewModel viewModel);
    GeneralServerResponse register(UserRegisterViewModel viewModel);
    List<UserListViewModel> getAllUsers();
    UserListViewModel getUserById(int id);
    GeneralServerResponse resetPassword(PasswordResetViewModel viewModel);
    GeneralServerResponse changePassword(PasswordChangeViewModel viewModel);
    GeneralServerResponse adminBlock(String username);
    GeneralServerResponse adminUnblock(String username);
    GeneralServerResponse loginUnblock(String username);
}
