package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@ToString(onlyExplicitlyIncluded = true)
@Getter
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @ToString.Include
    private final UUID id;

    @ToString.Include
    private String name;
    private String email;
    private String password;

    public UUID profileId;

    public final Instant createdAt;
    public Instant updatedAt;


    public User(String name, String email, String password) {
        this(UUID.randomUUID(), Instant.now(), name, email, password);
    }

    public User(UUID id, Instant createdAt, String name, String email, String password) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
        updatedAt = Instant.now();
    }

    public void setEmail(String email) {
        this.email = email;
        updatedAt = Instant.now();
    }
}
