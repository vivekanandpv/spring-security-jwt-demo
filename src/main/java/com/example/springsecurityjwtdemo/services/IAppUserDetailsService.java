package com.example.springsecurityjwtdemo.services;

import com.example.springsecurityjwtdemo.models.Role;
import com.example.springsecurityjwtdemo.models.User;
import com.example.springsecurityjwtdemo.viewmodels.PasswordChangeViewModel;
import com.example.springsecurityjwtdemo.viewmodels.PasswordResetViewModel;
import com.example.springsecurityjwtdemo.viewmodels.ProcessCompletionViewModel;
import com.example.springsecurityjwtdemo.viewmodels.UserRegisterViewModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IAppUserDetailsService extends UserDetailsService {
    boolean usernameExists(String username);

    User createUser(UserRegisterViewModel viewModel);

    ProcessCompletionViewModel adminBlockUser(String username);

    ProcessCompletionViewModel loginUnblockUser(String username);

    ProcessCompletionViewModel adminUnblockUser(String username);

    ProcessCompletionViewModel changePassword(PasswordChangeViewModel viewModel);

    ProcessCompletionViewModel resetPassword(PasswordResetViewModel viewModel);

    Optional<User> findUser(String username);

    Optional<User> findUserById(int id);

    Set<Role> getRolesByUsername(String username);

    List<User> getAllUsers();

    void logFailedLogin(String username);
}
