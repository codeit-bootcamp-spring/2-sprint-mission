package com.sprint.mission.discodeit.entity;

import java.util.*;
import java.util.stream.Collectors;

public class Channel extends BaseEntity {
    private String channelName;
    private TreeSet<User> participants;     //중복X && 이름순으로 정렬

    public Channel(String channelName) {
        validChannelName(channelName);

        this.channelName = channelName;
        this.participants = new TreeSet<>();
    }

    public String getChannelName() {
        return channelName;
    }

    public TreeSet<User> getParticipants() {
        return participants;
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
        super.updateUpdatedAt();
    }

    private void validChannelName(String channelName) {
        if (channelName == null || channelName.trim().isEmpty()) {
            throw new IllegalArgumentException("channelName 은 null 이거나 공백일 수 없다!!!");
        }
    }

    @Override
    public String toString() {
        return "\nChannel\n"
                + "channelName: " + channelName + '\n'
                + "participants:\n" + participants + '\n'
                + "id: " + super.getId() + '\n'
                + "createdAt: " + super.getCreatedAt() + '\n'
                + "updatedAt: " + super.getUpdatedAt() + '\n';
    }
}
