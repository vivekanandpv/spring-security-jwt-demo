package com.example.springsecurityjwtdemo.services;

import com.example.springsecurityjwtdemo.exceptions.RecordNotFoundException;
import com.example.springsecurityjwtdemo.models.Role;
import com.example.springsecurityjwtdemo.models.User;
import com.example.springsecurityjwtdemo.repositories.RoleRepository;
import com.example.springsecurityjwtdemo.repositories.UserRepository;
import com.example.springsecurityjwtdemo.security.AppUserDetails;
import com.example.springsecurityjwtdemo.utils.StaticDataProvider;
import com.example.springsecurityjwtdemo.viewmodels.PasswordChangeViewModel;
import com.example.springsecurityjwtdemo.viewmodels.PasswordResetViewModel;
import com.example.springsecurityjwtdemo.viewmodels.ProcessCompletionViewModel;
import com.example.springsecurityjwtdemo.viewmodels.UserRegisterViewModel;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AppUserDetailsService implements IAppUserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserDetailsService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException());

        return new AppUserDetails(user);
    }

    @Override
    public boolean usernameExists(String username) {
        return this.userRepository.existsByUsername(username);
    }

    @Override
    public User createUser(UserRegisterViewModel viewModel) {
        User user = new User();
        user.setUsername(viewModel.getUsername());

        user.setFirstName(viewModel.getFirstName());
        user.setLastName(viewModel.getLastName());
        user.setRegisteredOn(ZonedDateTime.now(ZoneId.of("UTC")));
        user.setDepartment(viewModel.getDepartment());
        user.setDesignation(viewModel.getDesignation());
        user.setEmail(viewModel.getEmail());
        user.setEmployeeId(viewModel.getEmployeeId());
        user.setPhoneNumber(viewModel.getPhoneNumber());
        user.setRemarks(viewModel.getRemarks());

        //  set a random strong password
        user.setPassword(this.passwordEncoder.encode(StaticDataProvider.getRandomResetKey(50)));
        user.setAdminBlock(false);
        user.setLoginBlock(true);
        user.setResetKey(StaticDataProvider.getRandomResetKey(8));

        Set<Role> claims;

        if (viewModel.getRoles() != null) {
            claims = viewModel.getRoles().stream()
                    .filter(c -> !this.roleRepository.findByRole(c).isEmpty())
                    .map(c -> this.roleRepository.findByRole(c).get())
                    .collect(Collectors.toSet());

        } else {
            claims = Set.of(this.roleRepository.findByRole("user").get());
        }

        user.setRoles(claims);

        return userRepository.saveAndFlush(user);
    }

    @Override
    public ProcessCompletionViewModel adminBlockUser(String username) {
        User user = this.findUser(username)
                .orElseThrow(RecordNotFoundException::new);

        user.setAdminBlock(true);
        user.setLoginBlock(true);
        user.setAdminBlockOn(ZonedDateTime.now(ZoneId.of("UTC")));
        user.setLoginBlockOn(ZonedDateTime.now(ZoneId.of("UTC")));

        this.userRepository.saveAndFlush(user);

        return new ProcessCompletionViewModel(true);
    }

    @Override
    public ProcessCompletionViewModel loginUnblockUser(String username) {
        User user = this.findUser(username)
                .orElseThrow(RecordNotFoundException::new);

        if (user.isAdminBlock()) {
            return new ProcessCompletionViewModel(false, List.of("User has admin block"), null);
        }

        if (!user.isLoginBlock()) {
            return new ProcessCompletionViewModel(false, List.of("User doesn't have login block"), null);
        }

        user.setLoginBlock(false);
        user.setPassword(this.passwordEncoder.encode(StaticDataProvider.getRandomResetKey(50)));

        String resetKey = StaticDataProvider.getRandomResetKey(8);
        user.setLoginBlockOn(null);
        user.setResetKey(resetKey);
        user.setPasswordResetOn(ZonedDateTime.now(ZoneId.of("UTC")));
        user.setFailedAttempts(0);

        this.userRepository.saveAndFlush(user);

        return new ProcessCompletionViewModel(true, null, resetKey);
    }

    @Override
    public ProcessCompletionViewModel adminUnblockUser(String username) {
        User user = this.findUser(username)
                .orElseThrow(RecordNotFoundException::new);

        if (!user.isAdminBlock()) {
            return new ProcessCompletionViewModel(false, List.of("User doesn't have admin block"), null);
        }

        user.setAdminBlock(false);
        user.setLoginBlock(false);
        user.setPassword(this.passwordEncoder.encode(StaticDataProvider.getRandomResetKey(50)));

        String resetKey = StaticDataProvider.getRandomResetKey(8);
        user.setResetKey(resetKey);
        user.setPasswordResetOn(ZonedDateTime.now(ZoneId.of("UTC")));
        user.setFailedAttempts(0);

        this.userRepository.saveAndFlush(user);

        return new ProcessCompletionViewModel(true);
    }

    @Override
    public ProcessCompletionViewModel changePassword(PasswordChangeViewModel viewModel) {
        User user = this.findUser(viewModel.getUsername())
                .orElseThrow(RecordNotFoundException::new);

        if (this.passwordEncoder.matches(viewModel.getCurrentPassword(), user.getPassword())) {
            user.setPassword(this.passwordEncoder.encode(viewModel.getNewPassword()));
            user.setPasswordChangedOn(ZonedDateTime.now(ZoneId.of("UTC")));

            this.userRepository.saveAndFlush(user);
            return new ProcessCompletionViewModel(true, null, null);
        } else {
            this.logFailedLogin(viewModel.getUsername());
            return new ProcessCompletionViewModel(false, List.of("Current password doesn't match"), null);
        }
    }

    @Override
    public ProcessCompletionViewModel resetPassword(PasswordResetViewModel viewModel) {
        if (!viewModel.getNewPassword().equals(viewModel.getConfirmNewPassword())) {
            return new ProcessCompletionViewModel(false, List.of("New passwords do not match"), null);
        }

        User user = this.findUser(viewModel.getUsername())
                .orElseThrow(RecordNotFoundException::new);

        if (viewModel.getResetKey().equals(user.getResetKey())) {
            user.setPassword(this.passwordEncoder.encode(viewModel.getNewPassword()));
            user.setFailedAttempts(0);
            user.setLoginBlock(false);
            user.setPasswordChangedOn(ZonedDateTime.now(ZoneId.of("UTC")));
            user.setResetKey(null);

            this.userRepository.saveAndFlush(user);

            return new ProcessCompletionViewModel(true);
        } else {
            this.logFailedLogin(viewModel.getUsername());
            return new ProcessCompletionViewModel(false, List.of("Reset key doesn't match"), null);
        }
    }

    @Override
    public Optional<User> findUser(String username) {
        return this.userRepository.findUserByUsername(username);
    }

    @Override
    public Optional<User> findUserById(int id) {
        return this.userRepository.findById(id);
    }

    @Override
    public Set<Role> getRolesByUsername(String username) {
        return this.userRepository.findRolesByUsername(username);
    }

    @Override
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    private void logSuccessfulLogin(String username) {
        User user = this.findUser(username)
                .orElseThrow(RecordNotFoundException::new);

        user.setLastLogin(ZonedDateTime.now(ZoneId.of("UTC")));
        user.setFailedAttempts(0);

        this.userRepository.saveAndFlush(user);
    }

    public void logFailedLogin(String username) {
        User user = this.findUser(username)
                .orElseThrow(RecordNotFoundException::new);

        user.setLastFailedLogin(ZonedDateTime.now(ZoneId.of("UTC")));

        int failedAttempts = user.getFailedAttempts();

        if (failedAttempts > StaticDataProvider.MAX_LOGIN_ATTEMPTS) {
            user.setLoginBlock(true);
            user.setLoginBlockOn(ZonedDateTime.now(ZoneId.of("UTC")));
        } else {
            user.setFailedAttempts(failedAttempts + 1);
        }

        this.userRepository.saveAndFlush(user);
    }

    private boolean isLoginAllowed(String username) {
        User user = this.findUser(username)
                .orElseThrow(RecordNotFoundException::new);

        return !(user.isLoginBlock() || user.isAdminBlock());
    }

    private boolean processLogin(String username, String rawPassword) {
        if (this.isLoginAllowed(username)) {
            User user = this.findUser(username)
                    .orElseThrow(RecordNotFoundException::new);

            if (this.passwordEncoder.matches(rawPassword, user.getPassword())) {
                this.logSuccessfulLogin(username);
                return true;
            } else {
                this.logFailedLogin(username);
                return false;
            }
        } else {
            return false;
        }
    }
}
