package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Channel {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;
    private String name;
    private final List<UUID> userIds = new ArrayList<>();

    public Channel(String name, UUID ownerId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now().getEpochSecond();
        this.updatedAt = createdAt;
        this.name = name;
        this.userIds.add(ownerId);
    }

    public void addMember(UUID userId) {
        userIds.add(userId);
        updatedAt();
    }

    public void updateName(String name) {
        this.name = name;
        updatedAt();
    }

    private void updatedAt() {
        this.updatedAt = Instant.now().getEpochSecond();
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
}
