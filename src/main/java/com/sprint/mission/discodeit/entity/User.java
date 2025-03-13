package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private final Long createdAt;
    private Long updatedAt;
    private String name;
    private String email;
    private String password;

    public User(String name, String email, String password) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now().toEpochMilli();
        this.updatedAt = createdAt;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void updateName(String name) {
        this.name = name;
        updateTimestamp();
    }

    public boolean isSameName(String name) {
        return this.name.equals(name);
    }

    public boolean isSameEmail(String email) {
        return this.email.equals(email);
    }

    private void updateTimestamp() {
        this.updatedAt = Instant.now().toEpochMilli();
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

    public String getEmail() {
        return email;
    }
}
