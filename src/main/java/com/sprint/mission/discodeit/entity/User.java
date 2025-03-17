package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.service.TimeFormatter;
import lombok.Getter;
import java.io.Serializable;
import java.time.Instant;

@Getter
public class User extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;

    public User(String name) {
        super();
        this.name = name;
    }

    public void update(String name, Instant updatedAt) {
        this.name = name;
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", createdAt=" + TimeFormatter.formatTimestamp(createdAt) +
                ", updatedAt=" + TimeFormatter.formatTimestamp(updatedAt) +
                '}';
    }
}
