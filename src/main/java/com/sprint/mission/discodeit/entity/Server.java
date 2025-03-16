package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ToString
@Getter
public class Server implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private UUID serverId;

    private UUID userOwnerId;

    private String name;
    private List<User> userList = new ArrayList<>();

    public final Instant createdAt;
    public Instant updatedAt;

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

    public void setServerId(UUID serverId) {
        this.serverId = serverId;
        updatedAt = Instant.now();
    }

    public void setUserOwnerId(UUID userOwnerId) {
        this.userOwnerId = userOwnerId;
        updatedAt = Instant.now();
    }

    public void setName(String name) {
        this.name = name;
        updatedAt = Instant.now();
    }
}

