package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Channel extends BaseEntity {
    private String channelName;
    private Set<User> participants;
    private List<Message> messages;

    public Channel(String channelName) {
        this.channelName = channelName;
        participants = new TreeSet<User>();
        messages = new ArrayList<>();
    }

    public String getChannelName() {
        return channelName;
    }

    public Set<User> getParticipants() {
        return participants;
    }

    public List<Message> getMessages() {
        return messages;
    }

    private void validChannelName(String channelName) {
        if (channelName == null || channelName.trim().isEmpty()) {
            throw new IllegalArgumentException("channelName 은 null 이거나 공백일 수 없다!!!");
        }
    }
}
