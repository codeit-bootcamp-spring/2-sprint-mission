package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Entity
@Table(name = "users")
public class User extends BaseUpdatableEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "username")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @OneToOne
    @JoinColumn(name = "profile_id")
    private BinaryContent binaryContent;

    protected User() {
    }

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
