package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id;

    public final Instant createdAt;
    public Instant updatedAt;

    private String name;
    private String password;

    public User(String name, String password) {
        this(UUID.randomUUID(), Instant.now(), name, password);
    }

    public User(UUID id, Instant createdAt, String name, String password) {
        this.id = id;
        this.createdAt = createdAt;
        this.name = name;
        this.password = password;
    }

}
