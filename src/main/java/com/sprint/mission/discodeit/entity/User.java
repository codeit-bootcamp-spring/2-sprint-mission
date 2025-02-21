package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public class User {
    private UUID id;
    private final Long createdAt;
    private Long updatedAt;
    private String name;
    private String password;

    public User(String name, String password) {
        this.id = UUID.randomUUID(); // TODO: 2/20/25 Id가 UUID에 의존하지 않도록 수정 요망
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

    public String getName() {
        return name;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }
}
