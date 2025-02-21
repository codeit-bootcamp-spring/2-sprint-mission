package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class User extends BaseEntity{
    private String username;
    private Set<Channel> joinedChannel;

    public User(String username) {
        super();
        this.username = username;
        this.joinedChannel = new HashSet<>();
    }

    public String getUsername() {
        return username;
    }

    public void updateUsername(String username) {
        this.username = username;
    }

    public Set<Channel> getJoinedChannel() {
        return joinedChannel;
    }

    public void updateJoinedChannel(Channel newChannel) {
        this.joinedChannel.add(newChannel);
    }
}
