package com.example.springsecurityjwtdemo.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
//  user is a keyword in SQL, hence renaming the table
@Table(name = "app_users")
public class User {
    @Id
    private int id;
    private String email;
    private String passwordHash;
    private int wrongAttempts;
    private boolean isBlocked;
    private LocalDateTime createdOn;
    private LocalDateTime blockedOn;
    private LocalDateTime resetOn;
    private String secretQuestion;
    private String secretAnswer;

    //  This is lazy otherwise
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="app_user_roles",
            joinColumns = {@JoinColumn(name="user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private Set<Role> roles = new HashSet<>();


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public int getWrongAttempts() {
        return wrongAttempts;
    }

    public void setWrongAttempts(int wrongAttempts) {
        this.wrongAttempts = wrongAttempts;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getBlockedOn() {
        return blockedOn;
    }

    public void setBlockedOn(LocalDateTime blockedOn) {
        this.blockedOn = blockedOn;
    }

    public LocalDateTime getResetOn() {
        return resetOn;
    }

    public void setResetOn(LocalDateTime resetOn) {
        this.resetOn = resetOn;
    }

    public String getSecretQuestion() {
        return secretQuestion;
    }

    public void setSecretQuestion(String secretQuestion) {
        this.secretQuestion = secretQuestion;
    }

    public String getSecretAnswer() {
        return secretAnswer;
    }

    public void setSecretAnswer(String secretAnswer) {
        this.secretAnswer = secretAnswer;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
