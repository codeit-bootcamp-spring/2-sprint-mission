package com.sprint.mission.discodeit.entity;


public class Message extends BaseEntity {
    private final String sender; // 메시지를 받는 사람
    private String message; // 메시지 내용

    public Message(String sender) {
        this.sender = sender;
    }

    public Message(String sender, String message) {
        this.sender = sender;
        this.message = message;

    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }


    public void updateMessage(String message) {
        this.message = message;
        super.update();
    }

    @Override
    public String toString() {
        return "보낸 사람: " + sender + "\n보낸 내용: " + message +
                "\n생성 시간: " + this.getCreatedAtFormatted() +
                "\n업데이트 시간: " + this.getupdatedAttFormatted() +
                "\n메시지 ID: " + this.getId() + "\n";
    }
}
