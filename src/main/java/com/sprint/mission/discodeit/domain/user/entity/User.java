package com.sprint.mission.discodeit.domain.user.entity;

import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.common.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.domain.userstatus.entity.UserStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseUpdatableEntity {

    @Column(name = "username")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @OneToOne
    @JoinColumn(name = "profile_id", columnDefinition = "uuid")
    private BinaryContent binaryContent;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private UserStatus userStatus;

    public User(String name, String email, String password, BinaryContent binaryContent) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.binaryContent = binaryContent;
        this.userStatus = new UserStatus(this, Instant.now());
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
        if (binaryContent != null && !binaryContent.getId().equals(this.binaryContent.getId())) {
            this.binaryContent = binaryContent;
        }
    }

    public boolean isSamePassword(String password) {
        return this.password.equals(password);
    }

}
