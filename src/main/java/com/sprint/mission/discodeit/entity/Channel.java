package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.groups.ChannelType;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Channel extends BaseEntity {
    private String name;
    private String description;
    private List<UUID> userIds = new ArrayList<>();
    private ChannelType channelType;

    public Channel(UUID id, Instant createdAt, UUID userId, ChannelType channelType) {
        super(id, createdAt);
        userIds.add(userId);
        this.channelType = channelType;
    }

    public Channel(UUID id, Instant createdAt, String name, String description, UUID userId, ChannelType channelType) {
        super(id, createdAt);
        this.name = name;
        this.description = description;
        userIds.add(userId);
        this.channelType = channelType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        return "Channel{" +
                super.toString() +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
