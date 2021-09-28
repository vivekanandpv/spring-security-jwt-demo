package com.example.springsecurityjwtdemo.repositories;

import com.example.springsecurityjwtdemo.models.Role;
import com.example.springsecurityjwtdemo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByUsername(String username);

    boolean existsByUsername(String username);

    @Query("SELECT user.roles FROM User user WHERE user.username = ?1")
    Set<Role> findRolesByUsername(String username);
}
