package com.sprint.mission.discodeit.Repository.jcf.impl;

import com.sprint.mission.discodeit.Repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.entity.Server;

import java.util.HashMap;
import java.util.LinkedList;

public class LinkedListJCFUserRepository extends JCFUserRepository {
    public LinkedListJCFUserRepository() {
        super.updateServerList(new LinkedList<>());
        super.updateMessageList(new HashMap<>());
    }

    @Override
    public void save(Server server) {
        super.save(server);
    }
}
