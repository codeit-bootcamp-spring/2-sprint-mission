package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;
    private String name;
    private String description;
    private ChannelType type;

    public Channel(ChannelType channelType, String name, String description) {
        this.id = UUID.randomUUID();
        this.createdAt = ZonedDateTime.now().toInstant();
        this.updatedAt = createdAt;
        this.name = name;
        this.description = description;
        this.type = channelType;
    }

    public void update(String name, String description) {
        if (this.type == ChannelType.PRIVATE) {
            throw new IllegalArgumentException("Private 파일은 수정할 수 없습니다.");
        }

        boolean anyValueUpdated = false;
        if (description != null && !description.equals(this.description)) {
            this.description = description;
            anyValueUpdated = true;
        }

        if (name != null && !name.equals(this.name)) {
            this.name = name;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            this.updatedAt = ZonedDateTime.now().toInstant();
        }
    }
}
