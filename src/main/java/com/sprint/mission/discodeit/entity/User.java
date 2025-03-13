package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class User extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Set<UUID> joinedChannels;
    private String username;

    public User(String username) {
        super();
        this.username = username;
        this.joinedChannels = new HashSet<>();
    }

    public void updateUsername(String username) {
        this.username = username;
        updateTimestamp();
    }

    public void addJoinedChannel(UUID channelId) {
        this.joinedChannels.add(channelId);
        updateTimestamp();

    }

    public void removeJoinedChannel(UUID channelId) {
        this.joinedChannels.remove(channelId);
        updateTimestamp();
    }

    public boolean isJoinedChannel(UUID channelId) {
        return this.joinedChannels.contains(channelId);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + getId() +
                ", username='" + username + '\'' +
                ", joinedChannels=" + joinedChannels +
                ", lastUpdateTime= " + getUpdatedAt() +
                '}';
    }

}
