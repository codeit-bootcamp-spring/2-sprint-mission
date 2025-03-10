package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Channel extends BaseEntity {
    private String name;
    private String description;

    public Channel(UUID id, long createdAt, String name, String description) {
        super(id, createdAt);
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
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
