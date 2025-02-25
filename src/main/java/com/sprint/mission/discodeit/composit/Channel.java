package com.sprint.mission.discodeit.composit;

import com.sprint.mission.discodeit.entity.Message;

import java.util.LinkedList;
import java.util.List;

public class Channel extends CategoryAndChannel {
    private List<Message> messageList;
    public Channel(String id, String name) {
        super(id, name);
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    public List<Message> getMessageList() {
        return messageList;
    }
}
