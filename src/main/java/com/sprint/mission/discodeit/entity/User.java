package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@ToString
@Getter
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;
    public UUID profileId;

    private String name;
    private String email;
    @ToString.Exclude
    private String password;

    public final Instant createdAt;
    public Instant updatedAt;


    public User(String name, String email, String password) {
        this(UUID.randomUUID(), null, Instant.now(), name, email, password);
    }

    public User(UUID id, UUID profileId, Instant createdAt, String name, String email, String password) {
        this.id = id;
        this.profileId = profileId;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void setId(UUID id) {
        this.id = id;
        updatedAt = Instant.now();
    }

    public void setName(String name) {
        this.name = name;
        updatedAt = Instant.now();
    }

    public void setEmail(String email) {
        this.email = email;
        updatedAt = Instant.now();
    }

    public void setProfileId(UUID profileId) {
        this.profileId = profileId;
        updatedAt = Instant.now();
    }
}
