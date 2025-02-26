package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Server;

import java.util.List;
import java.util.UUID;

public interface UserService {
    //생성
    public abstract Message write(UUID id, UUID targetId, String str);

    public abstract Message write(UUID id, UUID targetId, Message message);

    public abstract Server createServer(String name);

    //주입
    public abstract void addServer(UUID id, String name);

    public abstract void addServer(UUID id, Server server);

    //단건 조회
    public abstract Server getServer(UUID id, String name);

    // 출력
    public abstract void printServer(UUID id);

    public abstract void printServer(List<Server> list);

    // 삭제
    public abstract boolean removeServer(UUID id);

    public abstract boolean removeServer(UUID id, String targetName);

    public abstract boolean removeServer(List<Server> list, String targetName);

    //업데이트
    public abstract boolean updateServer(UUID id);

    public abstract boolean updateServer(UUID id, String targetName);

    public abstract boolean updateServer(List<Server> list, String targetName);

    public abstract boolean updateServer(List<Server> list, String targetName, String replaceName);

    public abstract boolean updateServer(UUID userId, String targetName, String replaceName);

}
