package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Channel implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private final Map<UUID, User> users;
    private final List<UUID> userIds;
    private final Map<UUID, Message> messages;
    private Instant updatedAt;
    private String name;
    private ChannelType type;

    public Channel(String name, ChannelType type) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        this.name = name;
        this.type = type;

        this.users = new HashMap<>();
        this.messages = new HashMap<>();
        this.userIds = new ArrayList<>();
    }

    public void updateName(String name) {
        this.name = name;
        this.updatedAt = Instant.now();
    }

    public void updateChannelType(ChannelType type) {
        this.type = type;
        this.updatedAt = Instant.now();
    }

    public void addUser(UUID userId) {
        userIds.add(userId);
    }
}
