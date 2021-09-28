package com.example.springsecurityjwtdemo.repositories;

import com.example.springsecurityjwtdemo.models.Role;
import com.example.springsecurityjwtdemo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByEmail(String email);

    boolean existsById(int id);

    @Query("SELECT user.roles FROM User user WHERE user.id = ?1")
    Set<Role> findRolesByUsername(int id);
}
