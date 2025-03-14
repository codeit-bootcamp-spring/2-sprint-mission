package com.sprint.mission.discodeit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class Channel extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String channelName;

    public void updateChannelName(String channelName) {
        super.updateTime();
        this.channelName = channelName;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "UUID= " + getId() +
                ", channelName='" + channelName +
                ", createdAt: " + getCreatedAt() +
                ", updatedAt: " + getUpdatedAt() +
                '}';
    }
}
