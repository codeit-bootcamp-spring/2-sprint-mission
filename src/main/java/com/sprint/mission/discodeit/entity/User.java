package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.service.TimeFormatter;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class User extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String email;
    private UUID profileId;


    public User(String username, String password, String email, UUID profileId) {
        super();
        this.username = username;
        this.password = password;
        this.email = email;
        this.profileId = profileId;
    }

    public void update(String username, String password, String email, Instant updatedAt) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='****'" +
                ", email='" + email + '\'' +
                ", id=" + id +
                ", createdAt=" + TimeFormatter.formatTimestamp(createdAt) +
                ", updatedAt=" + TimeFormatter.formatTimestamp(updatedAt) +
                '}';
    }
}
