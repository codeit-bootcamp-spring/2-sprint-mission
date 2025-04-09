package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class User implements Serializable, Comparable<User> {

    private static final long serialVersionUID = 1L;

    private UUID id;
    private Instant createdAt;
    private List<Message> messageList = new ArrayList<>();
    private Instant updatedAt;
    private String name;
    private String email;
    private String password;
    private UUID profileImageId;
    private Instant lastLoginAt;

    public User(String name, String email, String password) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        this.name = name;
        this.email = email;
        this.password = password;
        this.lastLoginAt = createdAt;
    }

    public void updateName(String name) {
        this.name = name;
        this.updatedAt = Instant.now();
    }

    public void updateEmail(String email) {
        this.email = email;
        this.updatedAt = Instant.now();
    }

    public void updateProfileImageId(UUID profileImageId) {
        this.profileImageId = profileImageId;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateLastLoginAt(Instant lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    @Override
    public int compareTo(User other) {
        return this.name.compareTo(other.name);
    }
}
