package com.sprint.mission.discodeit.Repository.file.impl;

import com.sprint.mission.discodeit.Repository.file.FileUserRepository;
import com.sprint.mission.discodeit.entity.Server;

import java.util.HashMap;
import java.util.LinkedList;

public class LinkedListFileUserRepository extends FileUserRepository {
    public LinkedListFileUserRepository() {
        super.setServerList(new LinkedList<>());
        super.setMessageList(new HashMap<>());
    }

    @Override
    public void save(Server server) {
        super.save(server);
    }
}
