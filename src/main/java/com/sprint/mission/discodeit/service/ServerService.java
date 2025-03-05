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

    public abstract void addChannel(UUID serverId, Channel channel);

    //단건 조회
    public abstract Channel getChannel(UUID id, String name);

    //출력
    public abstract void printChannel(UUID id);

    public abstract void printChannel(List<Container> list);

    //삭제
    public abstract boolean removeChannel(UUID id);

    public abstract boolean removeChannel(UUID id, String targetName);

    public abstract boolean removeChannel(List<Container> list, String targetName);

    //업데이트
    public abstract boolean updateChannel(UUID id);

    public abstract boolean updateChannel(UUID id, String targetName);

    public abstract boolean updateChannel(List<Container> list, String targetName);

    public abstract boolean updateChannel(List<Container> list, String targetName, String replaceName);

    public abstract boolean updateChannel(UUID userId, String targetName, String replaceName);

}
