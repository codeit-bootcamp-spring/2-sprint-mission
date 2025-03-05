package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Container.Channel;
import com.sprint.mission.discodeit.entity.Container.Container;
import com.sprint.mission.discodeit.entity.Server;

import java.util.List;
import java.util.UUID;

public interface ServerService {
    public abstract Channel createChannel(String name);

    //주입
    public abstract void addChannel(UUID serverId, String name);

    public abstract void addChannel(UUID serverId, Container channel);

    //단건 조회
    public abstract Container getChannel(UUID serverId, String name);

    //출력
    public abstract void printChannel(UUID serverId);

    //삭제
    public abstract boolean removeChannel(UUID serverId);

    public abstract boolean removeChannel(UUID serverId, String targetName);

    //업데이트
    public abstract boolean updateChannel(UUID serverId);

    public abstract boolean updateChannel(UUID serverId, String targetName);

    public abstract boolean updateChannel(UUID serverId, String targetName, String replaceName);

}
