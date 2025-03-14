package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private UUID profileId;
    private final Instant createdAt;
    private Instant updatedAt;
    private String username;
    private String email;
    private String password;

    private static final UUID DEFAULT_PROFILE_ID = UUID.fromString("00000000-0000-0000-0000-000000000000"); // 기본 프로필 이미지 ID

    public User(String username, String email, String password, UUID profileId) {
        this.id = UUID.randomUUID();
        this.profileId = profileId == null ? DEFAULT_PROFILE_ID : profileId;
        this.createdAt = Instant.now();
        this.username = username;
        this.email = email;
        this.password = password;
    }


    public void updateUserInfo(String newUsername, String newEmail, String newPassword) {
        boolean anyValueUpdated = false;
        if (newUsername != null && !newUsername.equals(this.username)) {
            this.username = newUsername;
            anyValueUpdated = true;
        }
        if (newEmail != null && !newEmail.equals(this.email)) {
            this.email = newEmail;
            anyValueUpdated = true;
        }
        if (newPassword != null && !newPassword.equals(this.password)) {
            this.password = newPassword;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            this.updatedAt = Instant.now();
        }
    }

    public void updateProfile(UUID profileId) {
        UUID newProfileId = profileId == null ? DEFAULT_PROFILE_ID : profileId;

        if(!this.profileId.equals(profileId)) {
            this.profileId = newProfileId;
            this.updatedAt = Instant.now();
        }
    }
}

