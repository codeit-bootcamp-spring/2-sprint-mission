package com.sprint.mission.discodeit.entity;


public class Message extends BaseEntity {
    private final String sender; // 메시지를 받는 사람
    private String message; // 메시지 내용

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
        System.out.printf("내용이 [ %s ] 로 변경되었습니다.", this.message);
    }

    @Override
    public String toString() {
        return "\nSender: " + sender + "\nMessage: " + message +
                "\nCreatedAt: " + this.getCreatedAtFormatted() +
                "\nUpdatedAt: " + this.getUpdatedAttFormatted() +
                "\nUUID: " + this.getId();
    }
}
