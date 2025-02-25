package com.sprint.mission.discodeit.Repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.LinkedList;
import java.util.List;

public class ChannelRepository {
    private final List<Message> messageList;

    public ChannelRepository() {
        this.messageList = new LinkedList<>();
    }

    public List<Message> getMessageList() {
        return messageList;
    }
}
