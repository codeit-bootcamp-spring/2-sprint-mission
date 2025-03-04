package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Server;

import java.util.List;
import java.util.UUID;

public interface UserService {
    //생성
    public abstract Server createServer(String name);

    //주입
    public abstract void addServer(UUID userId, String name);

    public abstract void addServer(UUID userId, Server server);

    //단건 조회
    public abstract Server getServer(UUID userId, String name);

    // 출력
    public abstract void printServer(UUID userId);

    public abstract void printServer(List<Server> list);

    // 삭제
    public abstract boolean removeServer(UUID userId);

    public abstract boolean removeServer(UUID userId, String targetName);

    //업데이트
    public abstract boolean updateServer(UUID userId);

    public abstract boolean updateServer(UUID userId, String targetName);

    public abstract boolean updateServer(UUID userId, String targetName, String replaceName);

}
