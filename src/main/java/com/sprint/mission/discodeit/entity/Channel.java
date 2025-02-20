package com.sprint.mission.discodeit.entity;

public class Channel extends BaseEntity {
    private String chatRoomName;

    public Channel(String chatRoomName) {
        super();
        this.chatRoomName = chatRoomName;
    }

    public String getChatRoomName() {
        return chatRoomName;
    }

    public void setChatRoomName(String chatRoomName) {
        this.chatRoomName = chatRoomName;
        this.updatedAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "채팅방 정보 : {" +
                "chatRoomName='" + chatRoomName + '\'' +
                ", id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
