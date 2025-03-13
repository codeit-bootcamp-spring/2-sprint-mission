package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Server;

import java.util.List;
import java.util.UUID;

public interface UserService {
    //생성
    UUID createServer(String name);

    //주입
    boolean joinServer(UUID userId, String name);

    //단건 조회
    Server getServer(UUID userId, String name);

    // 출력
    void printServer(UUID userId);

    // 삭제
    boolean removeServer(UUID userId, String targetName);

    //업데이트
    boolean updateServer(UUID userId, String targetName, String replaceName);

}
