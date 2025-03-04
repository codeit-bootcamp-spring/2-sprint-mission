package com.sprint.mission.discodeit.Repository;

import com.sprint.mission.discodeit.entity.Container.Container;

import java.util.List;

public interface ServerRepository {
    List<Container> getList();

    void setList(List<Container> list);

    void add(Container container);
}
