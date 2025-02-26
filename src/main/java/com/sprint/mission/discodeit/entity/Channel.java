package com.sprint.mission.discodeit.entity;

import java.util.*;

public class Channel extends BaseEntity {
    private String channelName;
    private Set<UUID> members;
    private Set<UUID> messages;

    public Channel(String channelName) {
        super();
        this.channelName = channelName;
        this.members = new HashSet<>();
        this.messages = new HashSet<>();
    }

    public String getChannelName() {
        return channelName;
    }

    public void updateChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Set<UUID> getMembers() {
        return members;
    }

    public void addMembers(UUID userId) {
        this.members.add(userId);
    }

    public void removeMember(UUID userId) {
        this.members.remove(userId);
    }

    public Set<UUID> getMessages() {
        return messages;
    }

    public void addMessages(UUID MessagesId) {
        this.messages.add(MessagesId);
    }

    public void removeMessage(UUID MessagesId) {
        this.messages.remove(MessagesId);
    }

    public boolean isUserInChannel(UUID userId){
        return this.members.contains(userId);
    }

    public boolean isMessageInChannel(UUID MessageId) {
        return this.messages.contains(MessageId);
    }

    @Override
    public String toString() {
        return "Channel{" +
                "channelName='" + channelName + '\'' +  // 오타 수정
                ", members=" + members +
                '}';
    }
}
