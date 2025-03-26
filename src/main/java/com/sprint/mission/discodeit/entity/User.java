package com.sprint.mission.discodeit.entity;

import lombok.Builder;
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

    // 클래스가 아니라 생성자에 붙여야 해당 값들에 대해 build가 가능
    // 클래스에 붙이면 모든 필드에 대해 build를 해줘야함 (안하면 null)
    // id, createdAt 같은 자동 생성해야 하는 필드도 개발자가 직접 입력해야 하는 문제가 발생
    @Builder
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

