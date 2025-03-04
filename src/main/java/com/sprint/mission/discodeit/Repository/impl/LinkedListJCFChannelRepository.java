package com.sprint.mission.discodeit.Repository.impl;

import com.sprint.mission.discodeit.Repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.entity.Message;

import java.util.LinkedList;

public class LinkedListJCFChannelRepository extends JCFChannelRepository {
    public LinkedListJCFChannelRepository() {
        super.setList(new LinkedList<>());
    }

    @Override
    public void add(Message message) {
        super.add(message);
    }
}
