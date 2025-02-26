package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Container.Channel;
import com.sprint.mission.discodeit.entity.Container.Container;

import java.util.List;
import java.util.UUID;

public interface ServerService {
    public abstract Channel createChannel(String name);

    public abstract void addChannel(UUID serverId, String name);

    public abstract void addChannel(UUID serverId, Channel channel);

    public abstract void printChannel(UUID id);

    public abstract void printChannel(List<Container> list);

    public abstract boolean removeChannel(UUID id, String targetName);

    public abstract boolean updateChannel(UUID id, String targetName, String replaceName);


}
