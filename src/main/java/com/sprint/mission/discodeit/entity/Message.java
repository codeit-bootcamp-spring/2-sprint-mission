package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public class Message extends SharedEntity {
    private String content;
    private final UUID userKey;
    private final UUID channelKey;
    private final String userName;
    private final String channelName;
    private final int messageId;

    public Message(int messageId, String content, UUID userKey, UUID channelKey, String userName, String channelName) {
        super();
        this.messageId = messageId;
        this.content = content;
        this.userKey = userKey;
        this.channelKey = channelKey;
        this.userName = userName;
        this.channelName = channelName;
    }

    public String getContent() {
        return content;
    }

    public void updateContent(String content) {
        this.content = content;
        setUpdatedAt(Instant.now().getEpochSecond());
    }

    public UUID getUserKey() {
        return userKey;
    }

    public UUID getChannelKey() {
        return channelKey;
    }

    public String getUserName() {
        return userName;
    }

    public String getChannelName() {
        return channelName;
    }

    public int getMessageId() {
        return messageId;
    }

    @Override
    public String toString() {
        return String.format("\n uuid= %s\n messageId= %s\n content= %s\n userName= %s\n channelName= %s\n createdAt= %s\n updatedAt= %s\n",
                uuid, messageId, content, userName, channelName, createdAt, updatedAt);
    }
}
