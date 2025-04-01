package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
public class User extends BaseEntity {

    private String name;
    private String email;
    private String password;
    private UUID profileId;

    public User(String name, String email, String password, UUID profileId) {
        super();
        this.name = name;
        this.email = email;
        this.password = password;
        this.profileId = profileId;
    }


    public void update(String name, String email, String password, UUID profileId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.profileId = profileId;
        super.update();
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
        return "\nName: " + name + "\nMail: " + email + "\nPassword: " + password +
                "\nUser ID: " + this.getId() +
                "\nProfile ID: " + profileId +
                "\nCreatedAt: " + this.getCreatedAtFormatted() +
                "\nUpdatedAt: " + this.getUpdatedAttFormatted();

    }
}
