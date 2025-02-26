package com.sprint.mission.discodeit.Repository.impl;

import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.entity.Server;

import java.util.HashMap;
import java.util.LinkedList;

public class LinkedListUserRepository extends UserRepository {
    public LinkedListUserRepository() {
        super.setServerList(new LinkedList<>());
        super.setMessageList(new HashMap<>());
    }

    @Override
    public void add(Server server) {
        super.add(server);
    }
}
