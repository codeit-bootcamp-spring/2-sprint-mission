package com.sprint.mission.discodeit.entity;

import java.util.*;
import java.util.stream.Collectors;

public class Channel extends BaseEntity {
    private String channelName;
    private TreeSet<User> participants;
    private List<Message> messages;

    public Channel(String channelName, User... participants) {
        validChannelName(channelName);

        this.channelName = channelName;
        if (participants == null || participants.length == 0) {     // 자기자신과의 채팅을 허용하냐 마냐 (허용하면 : == 0 / 허용하지 않으면 : <= 1
            throw new IllegalArgumentException("최소 1명의 사용자가 입력되어야 합니다!!!");
        }
        this.participants = Arrays.stream(participants)
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(User::getUserName))));       // Comparator.comparing(User::getUserName) (user1, user2) -> user1.getUserName().compareTo(user2.getUserName())
        messages = new ArrayList<>();
    }

    public String getChannelName() {
        return channelName;
    }

    public TreeSet<User> getParticipants() {
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

    @Override
    public String toString() {
        return "\nChannel\n"
                + "channelName: " + channelName + '\n'
                + "participants:\n" + participants + '\n'
                + "messages: " + messages + '\n'
                + "id: " + super.getId() + '\n'
                + "createdAt: " + super.getCreatedAt() + '\n'
                + "updatedAt: " + super.getUpdatedAt() + '\n';
    }
}
