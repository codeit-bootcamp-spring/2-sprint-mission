package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseUpdatableEntity {

    @Column(length = 50, unique = true, nullable = false)
    private String username;

    @Column(length = 100, unique = true, nullable = false)
    private String email;

    @Column(length = 60, nullable = false)
    private String password;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profile_id")
    private BinaryContent profile;

    @OneToOne(mappedBy = "user")
    private UserStatus status;

    public User(String username, String email, String password, BinaryContent profile) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.profile = profile;
    }


    public void update(String name, String email, String password, BinaryContent profile) {
        this.username = name;
        this.email = email;
        this.password = password;
        this.profile = profile;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof User user) {
            return user.getId().equals(this.getId());
        }
        return false;
    }

    @Override
    public String toString() {
        return "\nName: " + username + "\nMail: " + email + "\nPassword: " + password +
                "\nUser ID: " + this.getId() +
                "\nProfile ID: " + profile +
                "\nCreatedAt: " + this.getCreatedAt() +
                "\nUpdatedAt: " + this.getUpdatedAt();

    }
}
