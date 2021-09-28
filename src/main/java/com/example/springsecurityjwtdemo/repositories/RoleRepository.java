package com.example.springsecurityjwtdemo.repositories;

import com.example.springsecurityjwtdemo.models.Role;
import com.example.springsecurityjwtdemo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRole(String role);

    @Query("SELECT role.users FROM Role role WHERE role.role = ?1")
    List<User> findUsersByRole(String claim);
}
