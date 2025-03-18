package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
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
        updateLastModified();
    }

    public void updateName(String name) {
        this.name = name;
        updateLastModified();
    }

    private void updateLastModified() {
        this.updatedAt = Instant.now().toEpochMilli();
    }
}
