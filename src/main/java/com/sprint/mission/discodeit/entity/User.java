package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class User extends BaseUpdatableEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String email;
    private String password;
    private BinaryContent binaryContent;

    public User(String name, String email, String password, BinaryContent binaryContent) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.binaryContent = binaryContent;
    }

    public void update(String newUsername, String newEmail, String newPassword, BinaryContent binaryContent) {
        if (newUsername != null && !newUsername.equals(this.name)) {
            this.name = newUsername;
        }
        if (newEmail != null && !newEmail.equals(this.email)) {
            this.email = newEmail;
        }
        if (newPassword != null && !newPassword.equals(this.password)) {
            this.password = newPassword;
        }
        if (binaryContent.getId() != null && !binaryContent.getId().equals(this.binaryContent.getId())) {
            this.binaryContent = binaryContent;
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
