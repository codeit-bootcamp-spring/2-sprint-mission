package com.sprint.mission.discodeit.entity;

public class Message extends BaseEntity{
    private String messageContent;

    public Message(String messageContent) {
        super();
        this.messageContent = messageContent;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
        this.updatedAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "메세지 내용 : {" +
                "messageContent='" + messageContent + '\'' +
                ", id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }


}
