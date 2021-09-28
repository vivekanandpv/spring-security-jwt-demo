package com.example.springsecurityjwtdemo.services;

import com.example.springsecurityjwtdemo.exceptions.GeneralAuthenticationException;
import com.example.springsecurityjwtdemo.exceptions.RecordNotFoundException;
import com.example.springsecurityjwtdemo.models.User;
import com.example.springsecurityjwtdemo.utils.AppJwtUtils;
import com.example.springsecurityjwtdemo.viewmodels.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService implements IAuthService {
    private final AppUserDetailsService userDetailsService;
    private final AppJwtUtils jwtUtils;

    public AuthService(AppUserDetailsService userDetailsService, AppJwtUtils jwtUtils) {
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }


    @Override
    public TokenResponse getToken(String username) {
        String token = jwtUtils.generateToken(userDetailsService
                .loadUserByUsername(username));

        return new TokenResponse(token);
    }

    @Override
    public GeneralServerResponse register(UserRegisterViewModel viewModel) {
        if (this.userDetailsService.usernameExists(viewModel.getUsername())) {
            throw new GeneralAuthenticationException();
        }

        User user = this.userDetailsService.createUser(viewModel);

        return new GeneralServerResponse(true, user.getResetKey());
    }

    @Override
    public List<UserListViewModel> getAllUsers() {
        return this.userDetailsService
                .getAllUsers()
                .stream()
                .map(u -> {
                    UserListViewModel viewModel = new UserListViewModel();
                    BeanUtils.copyProperties(u, viewModel, "roles");
                    List<String> roles = new ArrayList<>();
                    u.getRoles().forEach(r -> {
                        roles.add(r.getRole());
                    });
                    viewModel.setRoles(roles);

                    return viewModel;
                })
                .sorted(Comparator.comparingInt(UserListViewModel::getId))
                .collect(Collectors.toList());
    }

    @Override
    public UserListViewModel getUserById(int id) {
        User user = this.userDetailsService.findUserById(id)
                .orElseThrow(RecordNotFoundException::new);

        UserListViewModel viewModel = new UserListViewModel();
        BeanUtils.copyProperties(user, viewModel, "roles");
        List<String> roles = new ArrayList<>();
        user.getRoles().forEach(r -> {
            roles.add(r.getRole());
        });
        viewModel.setRoles(roles);

        return viewModel;
    }

    @Override
    public GeneralServerResponse resetPassword(PasswordResetViewModel viewModel) {
        ProcessCompletionViewModel processCompletionViewModel = this.userDetailsService.resetPassword(viewModel);

        if (processCompletionViewModel.isCompleted()) {
            return new GeneralServerResponse(true, "Reset successful");
        } else {
            throw new GeneralAuthenticationException();
        }
    }

    @Override
    public GeneralServerResponse changePassword(PasswordChangeViewModel viewModel) {
        ProcessCompletionViewModel processCompletionViewModel = this.userDetailsService.changePassword(viewModel);

        if (processCompletionViewModel.isCompleted()) {
            return new GeneralServerResponse(true, "Password change successful");
        } else {
            throw new GeneralAuthenticationException();
        }
    }

    @Override
    public GeneralServerResponse adminBlock(String username) {
        ProcessCompletionViewModel processCompletionViewModel = this.userDetailsService.adminBlockUser(username);

        if (processCompletionViewModel.isCompleted()) {
            return new GeneralServerResponse(true, "Admin block successful");
        } else {
            throw new GeneralAuthenticationException();
        }
    }

    @Override
    public GeneralServerResponse adminUnblock(String username) {
        ProcessCompletionViewModel processCompletionViewModel = this.userDetailsService.adminUnblockUser(username);

        if (processCompletionViewModel.isCompleted()) {
            return new GeneralServerResponse(true, "Admin block successful");
        } else {
            throw new GeneralAuthenticationException();
        }
    }

    @Override
    public GeneralServerResponse loginUnblock(String username) {
        ProcessCompletionViewModel processCompletionViewModel = this.userDetailsService.loginUnblockUser(username);

        if (processCompletionViewModel.isCompleted()) {
            return new GeneralServerResponse(true, "Admin block successful");
        } else {
            throw new GeneralAuthenticationException();
        }
    }
}
