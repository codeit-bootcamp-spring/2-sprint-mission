package com.sprint.mission.discodeit.entity;

import java.util.HashSet;
import java.util.Set;

public class User extends BaseEntity{
    private String username;
    private Set<String> joinedChannels;

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

    public Set<String> getJoinedChannels() {
        return joinedChannels;
    }

    public void addJoinedChannel(String newChannel) {
        this.joinedChannels.add(newChannel);
    }

    public void removeJoinedChannel(String channelName) {
        this.joinedChannels.remove(channelName);
    }

    public boolean isJoinedChannel(String channelName) {
        return this.joinedChannels.contains(channelName);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + getId() + '\'' +
                ", username='" + username + '\'' +
                ", chnnels= " + joinedChannels +
                '}';
    }

}
