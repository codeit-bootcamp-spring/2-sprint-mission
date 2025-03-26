package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.User;

import java.time.Instant;
import java.util.UUID;

public class UserDto {
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private String username;
    private String email;
    private UUID profileId;
    private Boolean online;

    public UserDto(UUID id, Instant createdAt, Instant updatedAt, String username,
                   String email, UUID profileId, Boolean online) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.username = username;
        this.email = email;
        this.profileId = profileId;
        this.online = online;
    }

    public static UserDto from(User user) {
        return new UserDto(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getUsername(),
                user.getEmail(),
                user.getProfileId(),
                user.isOnline()
        );
    }

    public UUID getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public UUID getProfileId() {
        return profileId;
    }

    public Boolean getOnline() {
        return online;
    }
}