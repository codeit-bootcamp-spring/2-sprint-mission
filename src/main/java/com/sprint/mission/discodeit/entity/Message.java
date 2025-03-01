package com.sprint.mission.discodeit.entity;

public class Message extends SharedEntity {
    private static int totalMessageNum = 0;
    private int messageNum;
    private String content;
    private final String userName;
    private final String channelName;

    public Message(String content, String userName, String channelName) {
        super();
        this.messageNum = totalMessageNum++;
        this.content = content;
        this.userName = userName;
        this.channelName = channelName;

    }

    public int getMessageNum() {
        return messageNum;
    }

    public void setMessageNum(int messageNum) {
        this.messageNum = messageNum;
    }

    public int getTotalMessageNum() {
        return totalMessageNum;
    }


    public String getContent() {
        return content;
    }

    public void updateContent(String content) {
        this.content = content;
        setUpdatedAt(System.currentTimeMillis());
    }

    public String getUserName() {
        return userName;
    }

    public String getChannelName() {
        return channelName;
    }

    @Override
    public String toString() {
        return String.format("\n uuid= %s\n messageNum= %s\n content= %s\n userName= %s\n channelName= %s\n createdAt= %s\n updatedAt= %s\n",
                uuid, messageNum, content, userName, channelName, createdAt, updatedAt);
    }
}
