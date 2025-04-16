package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class User extends BaseUpdatableEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String email;
    private String password;

    private BinaryContent profile;
    private UserStatus status;

    public User(String username, String email, String password, BinaryContent profile) {

        this.username = username;
        this.email = email;
        this.password = password;
        this.profile = profile;

    }

    public void updateUser(String newUsername, String newEmail, String newPassword, BinaryContent newProfile) {
        if (newUsername != null && !newUsername.equals(this.username)) {
            this.username = newUsername;
        }
        if (newEmail != null && !newEmail.equals(this.email)) {
            this.email = newEmail;
        }
        if (newPassword != null && !newPassword.equals(this.password)) {
            this.password = newPassword;
        }
        if (newProfile != this.profile) {
            this.profile = newProfile;
        }
    }
}
