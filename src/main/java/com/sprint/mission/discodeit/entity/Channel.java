package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Getter
public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private ChannelType type;
    private String name;
    private String description;

    public Channel(ChannelType type, String name, String description) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.type = type;
        this.name = name;
        this.description = description;
    }

    public Channel(ChannelType type) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.type = type;
    }

    public void update(Optional<String> newName, Optional<String> newDescription) {
        boolean anyValueUpdated = false;
        if (newName.isPresent() && !newName.get().equals(this.name)) {
            this.name = newName.get();
            anyValueUpdated = true;
        }
        if (newDescription.isPresent() && !newDescription.get().equals(this.description)) {
            this.description = newDescription.get();
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            this.updatedAt = Instant.now();
        }
    }
}
