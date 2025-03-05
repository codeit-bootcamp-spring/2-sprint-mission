package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Channel extends Entity {
    private String name;
    private ChannelType type;
    private String description;

    public Channel(UUID uuid, String name, ChannelType type, String description) {
        super();
        this.name = name;
        this.type = type;
        this.description = description;
    }

    public String getName() { return name; }
    public void updateName(String name) {
        this.name = name;
        this.updatedAt = System.currentTimeMillis();
    }
    public ChannelType getType() { return type; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public void setType(ChannelType type) { this.type = type; }
}