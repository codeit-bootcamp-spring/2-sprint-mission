package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Container.Container;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    public abstract Message write(UUID id, String name);

    public abstract void printChannel(UUID id);

    public abstract void printChannel(List<Message> list);

    public abstract boolean removeMessage(UUID id, String targetName);

    public abstract boolean updateMessage(UUID id, String targetName, String replaceName);

}
