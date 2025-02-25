package com.sprint.mission.discodeit.Repository;

import com.sprint.mission.discodeit.entity.Container.Container;

import java.util.LinkedList;
import java.util.List;

public class ServerRepository {
    private final List<Container> containers;

    public ServerRepository() {
        containers = new LinkedList<>();
    }

    public List<Container> getContainers() {
        return containers;
    }
}
