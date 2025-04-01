package com.sprint.mission.discodeit.core.server.entity;

import com.sprint.mission.discodeit.core.user.entity.User;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
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

    private UUID userId;

    private String name;
    private List<User> userList = new ArrayList<>();

    public final Instant createdAt;
    public Instant updatedAt;

    public Server(UUID userId, String name) {
        this(UUID.randomUUID(), userId, Instant.now(), name);
    }

    public Server(UUID serverId, UUID userId, Instant createdAt, String name) {
        this.serverId = serverId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
        this.name = name;
    }

    public void setServerId(UUID serverId) {
        this.serverId = serverId;
        updatedAt = Instant.now();
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
        updatedAt = Instant.now();
    }

    public void setName(String name) {
        this.name = name;
        updatedAt = Instant.now();
    }
}

