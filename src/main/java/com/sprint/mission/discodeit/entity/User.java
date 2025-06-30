package com.sprint.mission.discodeit.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseUpdatableEntity implements Serializable {

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 60)
    private String password;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", columnDefinition = "UUID")
    private BinaryContent profile;

    @JsonManagedReference
    @Setter(AccessLevel.PROTECTED)
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public User(String username, String email, String password, BinaryContent profile) {

        this.username = username;
        this.email = email;
        this.password = password;
        this.profile = profile;
        this.role = Role.ROLE_USER;

    }

    public void updateUser(String newUsername, String newEmail, String newPassword,
        BinaryContent newProfile) {
        if (newUsername != null && !newUsername.equals(this.username)) {
            this.username = newUsername;
        }
        if (newEmail != null && !newEmail.equals(this.email)) {
            this.email = newEmail;
        }
        if (newPassword != null) {
            this.password = newPassword;
        }
        if (newProfile != this.profile) {
            this.profile = newProfile;
        }
    }

    public void updateRole(Role newRole) {
        if (this.role != newRole) {
            this.role = newRole;
        }
    }
}
