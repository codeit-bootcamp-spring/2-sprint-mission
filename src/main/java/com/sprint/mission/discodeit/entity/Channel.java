package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;
import java.io.Serializable;
import lombok.Getter;

@Getter
public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAtSeconds;
    private Instant updatedAtSeconds;
    private ChannelType type;
    private String name;
    private String description;

    public Channel(ChannelType type, String name, String description) {
        this.id = UUID.randomUUID();
        this.createdAtSeconds = Instant.now();
        this.type = type;
        this.name = name;
        this.description = description;
    }

    public void update(String newName, String newDescription) {
        boolean anyValueUpdated = false;
        if (newName != null && !newName.equals(this.name)) {
            this.name = newName;
            anyValueUpdated = true;
        }
        if (newDescription != null && !newDescription.equals(this.description)) {
            this.description = newDescription;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            this.updatedAtSeconds = Instant.now();
        }
    }
}
