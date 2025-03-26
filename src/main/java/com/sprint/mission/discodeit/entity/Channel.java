package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class Channel extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private String title;
    private String description;
    private ChannelType channelType;

    public Channel(ChannelType channelType, String title, String description) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.description = description;
        this.channelType = channelType;
    }

    public void updateChannel(String newTitle, String newDescription, Instant updateAt) {
        boolean isChanged = false;
        if (!newTitle.equals(this.title)) {
            this.title = newTitle;
            isChanged = true;
        }
        if (!newDescription.equals(this.description)) {
            this.description = newDescription;
            isChanged = true;
        }
        if (isChanged) {
            super.updateUpdatedAt(updateAt);
        }
    }

    @Override
    public String toString() {
        return "Channel{id:" + id + ",title:" + title +  ",createdAt:" + getCreatedAt() + ",updatedAt:" + getUpdatedAt() + "}";
    }

}
