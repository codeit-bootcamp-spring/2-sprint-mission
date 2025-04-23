package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.common.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends BaseUpdatableEntity implements Persistable<UUID> {

    private String username;

    private String email;

    private String password;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profile_id")
    private BinaryContent profile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserStatus status;

    @Transient
    private boolean isNew = true;

    protected User() {
    }

    public static User createUser(String username, String email, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password); // TODO 암호화 처리

        return user;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
        status.setUser(this);
    }

    public BinaryContent updateProfile(BinaryContent newProfile) {

        BinaryContent oldProfile = this.profile;
        this.profile = newProfile;

        return oldProfile;
    }

    public void update(String newUsername, String newEmail, String newPassword) {
        if (newUsername != null && !newUsername.equals(this.username)) {
            this.username = newUsername;
        }
        if (newEmail != null && !newEmail.equals(this.email)) {
            this.email = newEmail;
        }
        if (newPassword != null && !newPassword.equals(this.password)) {
            this.password = newPassword;
        }
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostLoad
    @PrePersist
    void markNotNew() {
        this.isNew = false;
    }
}
