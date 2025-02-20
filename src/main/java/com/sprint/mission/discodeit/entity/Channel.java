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

    public void updateChannelName(String newChannelName) {
        validChannelName(newChannelName);
        this.channelName = newChannelName;
        super.updateUpdatedAt();
    }

    public void addParticipant(User newParticipant) {
        if (newParticipant == null) {
            throw new IllegalArgumentException("newParticipant 는 null 이 될 수 없습니다!!!");
        }
        if (this.participants.contains(newParticipant)) {
            throw new IllegalArgumentException("newParticipant 는 이미 채널에 참여 중 입니다!!! ");
        }
        this.participants.add(newParticipant);
        super.updateUpdatedAt();
    }

    public void addMessage(Message newMessage) {
        if (newMessage == null) {
            throw new IllegalArgumentException("newMessage 는 null 이 될 수 없습니다!!!");
        }
        this.messages.add(newMessage);
        super.updateUpdatedAt();
    }

    private void validChannelName(String channelName) {
        if (channelName == null || channelName.trim().isEmpty()) {
            throw new IllegalArgumentException("channelName 은 null 이거나 공백일 수 없다!!!");
        }
    }
}
