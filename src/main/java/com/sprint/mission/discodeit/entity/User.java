package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;

    private String userName;
    private String email;
    private String password;

    @Setter
    private UUID profileId;

    public User(String userName, String email, String password) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();

        this.userName = userName;
        this.email = email;
        this.password = password;
        this.profileId = null;

    }

    public void updateUser(String userName, String email, String password, UUID profileId) {
        if (userName != null) {
            this.userName = userName;
        }
        if (email != null) {
            this.email = email;
        }
        if (password != null) {
            this.password = password;
        }
        if (profileId != null) {
            this.profileId = profileId;
        }
        this.updatedAt = Instant.now();
    }

    public String toString() {
        return "[ userID: " + getId() + "userName: " + getUserName() + " ]";
    }
}
