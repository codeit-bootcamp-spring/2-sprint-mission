package com.sprint.mission.discodeit.entity;

import java.util.HashSet;
import java.util.Set;

public class Channel {
    private String channelname;
    private Set<User> members;
    private Set<Message> messages;

    public Channel(String channelname) {
        super();
        this.channelname = channelname;
        this.members = new HashSet<>();
        this.messages = new HashSet<>();
    }

    public String getChannelname() {
        return channelname;
    }

    public void updateChannelname(String channelname) {
        this.channelname = channelname;
    }

    public Set<User> getMembers() {
        return members;
    }

    public void updateMembers(User newMember) {
        this.members.add(newMember);
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void updateMessages(Message newMessages) {
        this.messages.add(newMessages);
    }
}
