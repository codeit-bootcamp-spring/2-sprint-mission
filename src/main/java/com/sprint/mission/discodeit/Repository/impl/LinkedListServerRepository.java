package com.sprint.mission.discodeit.Repository.impl;

import com.sprint.mission.discodeit.Repository.ServerRepository;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.entity.Container.Container;

import java.util.LinkedList;

public class LinkedListServerRepository extends ServerRepository {
    public LinkedListServerRepository() {
        super.setList(new LinkedList<>());
    }

    @Override
    public void add(Container container) {
        super.add(container);
    }
}
