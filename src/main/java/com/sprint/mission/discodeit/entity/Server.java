package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.UUID;

@ToString
@Getter
public class Server implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID serverId;
    private final UUID userOwnerId;
    public final Instant createdAt;
    public Instant updatedAt;
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd a hh:mm:ss.SS");

    private String name;

    public Server(UUID userOwnerId, String name) {
        this(UUID.randomUUID(), userOwnerId, Instant.now(), name);
    }

    public Server(UUID serverId, UUID userOwnerId, Instant createdAt, String name) {
        this.serverId = serverId;
        this.userOwnerId = userOwnerId;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
        updatedAt = Instant.now();
    }
}

