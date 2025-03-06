package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class Message extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 3L;
    private UUID senderId;
    private String content;
    private UUID channelId;
    private boolean isEdited;

    public Message(UUID senderId, String content, UUID channelId) {
        super();
        validateContent(content);
        this.senderId = senderId;
        this.content = content;
        this.channelId = channelId;
        this.isEdited = false;
    }

    public UUID getSenderId() {
        return this.senderId;
    }

    public String getContent() {
        return this.content;
    }

    public UUID getChannelId() {
        return this.channelId;
    }

    public boolean isEdited() {
        return this.isEdited;
    }

    public void updateContent(String newContent) {
        validateContent(newContent);
        this.content = newContent;
        this.isEdited = true;
        super.updateUpdatedAt();
    }

    public static void validateContent(String content) {
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("Content는 null 이거나 길이가 0일 수 없다!!!");
        }
    }

    @Override
    public String toString() {
        return "\nMessage\n"
                + "senderId: " + this.senderId + "\n"
                + "content: " + this.content + "\n"
                + "channelId: " + this.channelId + "\n"
                + "isEdited: " + this.isEdited + "\n"
                + super.toString() + "\n";
    }
}
