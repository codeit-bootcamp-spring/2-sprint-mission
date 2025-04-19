package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter
public class User extends BaseUpdatableEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String email;
    private String password;
    private UUID profileId;

    public User(String name, String email, String password, UUID profileId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.profileId = profileId;
    }

    public void update(String newUsername, String newEmail, String newPassword, UUID newProfileId) {
        if (newUsername != null && !newUsername.equals(this.name)) {
            this.name = newUsername;
        }
        if (newEmail != null && !newEmail.equals(this.email)) {
            this.email = newEmail;
        }
        if (newPassword != null && !newPassword.equals(this.password)) {
            this.password = newPassword;
        }
        if (newProfileId != null && !newProfileId.equals(this.profileId)) {
            this.profileId = newProfileId;
        }
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
}
