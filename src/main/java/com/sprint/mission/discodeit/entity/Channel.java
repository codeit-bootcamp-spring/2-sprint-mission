package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Channel extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Set<UUID> members;
    private final Set<UUID> messages;
    private String channelName;

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
        updateTimestamp();
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

    public boolean isMessageInChannel(UUID messageId) {
        return this.messages.contains(messageId);
    }

    @Override
    public String toString() {
        return "Channel{" +
                "channelName='" + channelName + '\'' +  // 오타 수정
                ", members=" + members +
                '}';
    }
}
