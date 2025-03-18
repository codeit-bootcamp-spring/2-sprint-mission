package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.service.TimeFormatter;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;

@Getter
public class User extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String email;

    public User(String name, String email) {
        super();
        this.name = name;
        this.email = email;
    }

    public void update(String name, String email, Instant updatedAt) {
        this.name = name;
        this.email = email;
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", id=" + id +
                ", createdAt=" + TimeFormatter.formatTimestamp(createdAt) +
                ", updatedAt=" + TimeFormatter.formatTimestamp(updatedAt) +
                '}';
    }
}
