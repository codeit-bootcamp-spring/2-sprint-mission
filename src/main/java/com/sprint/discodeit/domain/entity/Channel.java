package com.sprint.discodeit.domain.entity;

import com.sprint.discodeit.domain.ChannelType;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;


@Getter
public class Channel implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    //
    private ChannelType type;
    private String name;
    private String description;

    @Builder
    public Channel(ChannelType type, String name, String description) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = null;
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
            this.updatedAt = Instant.now();
        }
    }
}
