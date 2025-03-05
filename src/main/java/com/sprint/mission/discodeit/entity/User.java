package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public class User {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    private String username;
    private String email;
    private String password;

    public User(String username, String email, String password) {
        this.id = UUID.randomUUID();

        this.createdAt = Instant.now().getEpochSecond();
        this.updatedAt = null;

        this.username = username;
        this.email = email;
        this.password = password;
    }


    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // update
    public void update(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.updatedAt = Instant.now().getEpochSecond();
    }
}

