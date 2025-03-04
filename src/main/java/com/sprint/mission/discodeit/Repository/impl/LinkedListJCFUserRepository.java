package com.sprint.mission.discodeit.Repository.impl;

import com.sprint.mission.discodeit.Repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.entity.Server;

import java.util.HashMap;
import java.util.LinkedList;

public class LinkedListJCFUserRepository extends JCFUserRepository {
    public LinkedListJCFUserRepository() {
        super.setServerList(new LinkedList<>());
        super.setMessageList(new HashMap<>());
    }

    @Override
    public void add(Server server) {
        super.add(server);
    }
}
