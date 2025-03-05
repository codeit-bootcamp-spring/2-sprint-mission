package com.sprint.mission.discodeit.Repository;

import com.sprint.mission.discodeit.entity.Container.Container;

import java.util.List;

public interface ServerRepository {
    void save(Container container);

    List<Container> getContainerList();

    void setContainerList(List<Container> containerList);

}
