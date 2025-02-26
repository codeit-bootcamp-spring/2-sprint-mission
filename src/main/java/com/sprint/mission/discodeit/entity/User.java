package com.sprint.mission.discodeit.entity;

import java.util.*;

public class User extends BaseEntity{
    private String username;
    private Set<UUID> joinedChannels;

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
    }

    public Set<UUID> getJoinedChannels() {
        return joinedChannels;
    }

    public void addJoinedChannel(UUID channelId) {
        this.joinedChannels.add(channelId);
    }

    public void removeJoinedChannel(UUID channelId) {
        this.joinedChannels.remove(channelId);
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
                '}';
    }

}
