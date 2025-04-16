package com.sprint.mission.discodeit.entity;

import jdk.incubator.vector.VectorOperators;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class User extends BaseUpdatableEntity {

    private String username;
    private String email;
    private String password;

    private BinaryContent profile;

    @Setter
    private UserStatus userStatus;

    @Builder
    public User(String username, String email, String password, BinaryContent profile) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.profile = profile;
    }

    public void update(String newUsername, String newEmail, String newPassword, BinaryContent profile) {
        if (newUsername != null && !newUsername.equals(this.username)) {
            this.username = newUsername;
        }
        if (newEmail != null && !newEmail.equals(this.email)) {
            this.email = newEmail;
        }
        if (newPassword != null && !newPassword.equals(this.password)) {
            this.password = newPassword;
        }
        if (profile != null && !profile.equals(this.profile)) {
            this.profile = profile;
        }
    }
}
