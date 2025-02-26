package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.UUID;

public interface MessageService {
    //전송
    public abstract void send(UUID myId, UUID targetId, String str);

    public abstract void send(UUID myId, UUID targetId, Message message);

    //읽기
    public abstract void read(UUID myId);

    //삭제
    public abstract boolean remove(UUID myId, UUID targetId);

    public abstract boolean remove(UUID myId, UUID targetId, String str);

    public abstract boolean remove(UUID myId, UUID targetId, Message message);
    //업데이트

}
