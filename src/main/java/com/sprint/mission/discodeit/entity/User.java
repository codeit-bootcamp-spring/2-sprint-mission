package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.service.TimeFormatter;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;

@Getter
public class User extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String email;


    public User(String username, String password, String email) {
        super();
        this.username = username;
        this.password = password;
        this.email = email;

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
