package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public class User {
    private UUID id;
    private final Long createdAt;
    private Long updatedAt;
    private String name;
    private String email;
    private String password;

    public User(String name, String email, String password) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now().getEpochSecond();
        this.updatedAt = createdAt;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void updateName(String name) {
        this.name = name;
        updatedAt();
    }

    public boolean isSameName(String name) {
        return this.name.equals(name);
    }

    public boolean isSameEmail(String email) {
        return this.email.equals(email);
    }

    private void updatedAt() {
        this.updatedAt = Instant.now().getEpochSecond();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }
}
