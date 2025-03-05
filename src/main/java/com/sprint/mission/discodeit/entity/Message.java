package com.sprint.mission.discodeit.entity;

public class Message extends SharedEntity {
    private String content;
    private final String userName;
    private final String channelName;
    private final int messageId;

    public Message(int messageId, String content, String userName, String channelName) {
        super();
        this.messageId = messageId;
        this.content = content;
        this.userName = userName;
        this.channelName = channelName;
    }

    public void updateContent(String content) {
        this.content = content;
        setUpdatedAt(System.currentTimeMillis());
    }

    public int getMessageId() {
        return messageId;
    }

    public String getChannelName() {
        return channelName;
    }

    @Override
    public String toString() {
        return String.format("\n uuid= %s\n messageId= %s\n content= %s\n userName= %s\n channelName= %s\n createdAt= %s\n updatedAt= %s\n",
                uuid, messageId, content, userName, channelName, createdAt, updatedAt);
    }
}
