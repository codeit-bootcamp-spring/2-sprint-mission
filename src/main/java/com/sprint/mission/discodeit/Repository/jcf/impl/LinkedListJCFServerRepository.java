package com.sprint.mission.discodeit.Repository.jcf.impl;

import com.sprint.mission.discodeit.Repository.jcf.JCFServerRepository;
import com.sprint.mission.discodeit.entity.Container.Container;

import java.util.LinkedList;

public class LinkedListJCFServerRepository extends JCFServerRepository {
    public LinkedListJCFServerRepository() {
        super.updateContainerList(new LinkedList<>());
    }

    @Override
    public void save(Container container) {
        super.save(container);
    }
}
