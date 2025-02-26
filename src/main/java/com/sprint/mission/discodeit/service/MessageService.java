package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.UUID;

public interface MessageService {
    public abstract void send(UUID id, UUID targetId, String str);

    public abstract void send(UUID id, UUID targetId, Message message);

}
