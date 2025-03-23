package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
public class User implements Serializable {
    private static final long SERIAL_VERSION_UID = 1L;
    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;
    private String name;
    private String email;
    private String password;
    private UUID profileId;
    private UserStatus userStatus;

    public User(String name, String email, String password, UUID profileId) {
        this.id = UUID.randomUUID();
        this.createdAt = ZonedDateTime.now().toInstant();
        this.updatedAt = createdAt;
        this.name = name;
        this.email = email;
        this.password = password;
        this.profileId = profileId;
        this.userStatus = new UserStatus(this.id);
    }

    public void updateName(String name) {
        this.name = name;
        updateLastModified();
    }

    public void updateProfileImage(UUID profileId) {
        this.profileId = profileId;
        updateLastModified();
    }

    public boolean isSameName(String name) {
        return this.name.equals(name);
    }

    public boolean isSameEmail(String email) {
        return this.email.equals(email);
    }

    public boolean isSamePassword(String password) {
        return this.password.equals(password);
    }

    public void changeUserStatus() {
        this.userStatus.isLogin(ZonedDateTime.now().toInstant());
    }

    private void updateLastModified() {
        this.updatedAt = ZonedDateTime.now().toInstant();
    }
}
