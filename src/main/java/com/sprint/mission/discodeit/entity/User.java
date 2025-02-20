package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public class User {
    private UUID id;
    private final Long createdAt;
    private Long updatedAt;
    private String name;
    private String password;

    public User(UUID id, String name, String password) {
        this.id = id;
        this.createdAt = Instant.now().getEpochSecond();
        this.updatedAt = createdAt;
        this.name = name;
        this.password = password;
    }

    public void updateName(String name) {
        this.name = name;
        updatedAt();
    }

    public boolean isSameName(String name) {
        return this.name.equals(name);
    }

    private void updatedAt() {
        this.updatedAt = Instant.now().getEpochSecond();
    }

    public UUID getId() {
        return id;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public String getName() {
        return name;
    }
}
