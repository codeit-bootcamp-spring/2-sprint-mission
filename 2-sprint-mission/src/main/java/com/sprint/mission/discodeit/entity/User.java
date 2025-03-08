package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable {
    private final UUID id;
    private final long createdAt;
    private long updatedAt;
    private final String email;
    private String password;
    private String nickname;
    private UserStatus status;
    private UserRole role;
    private static final long serialVersionUID = 1L;

    public User(String email, String password, String nickname, UserStatus status, UserRole role) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.status = status;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public UserStatus getStatus() {
        return status;
    }

    public UserRole getRole() {
        return role;
    }

    public void updatePassword(String password) {
        this.password = password;
        updateTimestamp();
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
        updateTimestamp();
    }

    public void updateStatus(UserStatus status) {
        this.status = status;
        updateTimestamp();
    }

    public void updateRole(UserRole role) {
        this.role = role;
        updateTimestamp();
    }

    protected final void updateTimestamp() {
        this.updatedAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", status=" + status +
                ", role=" + role +
                '}';
    }

}
