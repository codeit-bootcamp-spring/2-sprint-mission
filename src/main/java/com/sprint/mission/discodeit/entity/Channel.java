package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;
    private String name;
    private final List<UUID> userIds = new ArrayList<>();

    public Channel(String name, UUID ownerId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now().toEpochMilli();
        this.updatedAt = createdAt;
        this.name = name;
        this.userIds.add(ownerId);
    }

    public void addMember(UUID userId) {
        userIds.add(userId);
        updateTimestamp();
    }

    public void updateName(String name) {
        this.name = name;
        updateTimestamp();
    }

    private void updateTimestamp() {
        this.updatedAt = Instant.now().toEpochMilli();
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public List<UUID> getUserIds() {
        return Collections.unmodifiableList(userIds);
    }

    public Long getCreatedAt() {
        return createdAt;
    }
}
