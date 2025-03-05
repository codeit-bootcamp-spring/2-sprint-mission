package com.sprint.mission.discodeit.Repository.jcf;

import com.sprint.mission.discodeit.Repository.ServerRepository;
import com.sprint.mission.discodeit.entity.Container.Container;

import java.util.List;

public class JCFServerRepository implements ServerRepository  {
    private List<Container> list;

    public void save(Container container) {
        list.add(container);
    }

    public List<Container> getContainerList() {
        return list;
    }

    public void setContainerList(List<Container> containerList) {
        this.list = containerList;
    }
}
