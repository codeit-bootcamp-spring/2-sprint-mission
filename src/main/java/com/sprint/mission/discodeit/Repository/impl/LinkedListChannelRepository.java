package com.sprint.mission.discodeit.Repository.impl;

import com.sprint.mission.discodeit.Repository.ChannelRepository;
import com.sprint.mission.discodeit.entity.Message;

import java.util.LinkedList;

public class LinkedListChannelRepository extends ChannelRepository {
    public LinkedListChannelRepository() {
        super.setList(new LinkedList<>());
    }

    @Override
    public void add(Message message) {
        super.add(message);
    }
}
