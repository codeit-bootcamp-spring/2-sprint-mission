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
    private ChannelType type;

    public Channel(ChannelType channelType, String name) {
        this.id = UUID.randomUUID();
        this.createdAt = ZonedDateTime.now().toInstant();
        this.updatedAt = createdAt;
        this.name = name;
        this.type = channelType;
    }

    public void updateName(String name) {
        if (this.type == ChannelType.PRIVATE) {
            throw new IllegalArgumentException("Private 파일은 수정할 수 없습니다.");
        }

        this.name = name;
        updateLastModified();
    }

    private void updateLastModified() {
        this.updatedAt = ZonedDateTime.now().toInstant();
    }
}
