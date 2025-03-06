package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class User extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Set<UUID> joinedChannels;
    private String username;

    public User(String username) {
        super();
        this.username = username;
        this.joinedChannels = new HashSet<>();
    }

    public String getUsername() {
        return username;
    }

    public void updateUsername(String username) {
        this.username = username;
        updateTimestamp();
    }

    public Set<UUID> getJoinedChannels() {
        return joinedChannels;
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
