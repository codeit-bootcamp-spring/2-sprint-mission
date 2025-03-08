package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Channel extends BaseEntity implements Serializable {
    private String channelName;
    private final Set<User> members = new HashSet<>();;

    @Serial
    private static final long serialVersionUID = 1L;

    public Channel(String channelName) {
        super();
        this.channelName = channelName;
    }

    public Set<User> getMembers() {
        return members;
    }

    public void addMember(User user) {
        members.add(user);
    }

    public void removeMember(User user) {
        members.remove(user);
    }

    public boolean isMember(User user) {
        return members.contains(user);
    }

    public String getChannelName() {
        return channelName;
    }

    public void updateChannelName(String channelName) {
        this.channelName = channelName;
    }

    @Override
    public String toString() {
        return channelName;
    }
}
