package com.sprint.mission.discodeit.entity;

import java.util.List;

public class Channel extends Container {
    private List<Message> messageList;
    public Channel(String name) {
        super(name);
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    public List<Message> getMessageList() {
        return messageList;
    }
}
