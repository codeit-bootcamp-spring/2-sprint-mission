package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Server;

import java.util.List;
import java.util.UUID;

/**
 * 사용자 서비스 인터페이스.
 * 사용자가 서버를 생성, 가입, 조회, 삭제, 수정할 수 있도록 제공하는 서비스.
 */
public interface UserService {
    /**
     * 새로운 서버를 생성합니다.
     *
     * @param name 생성할 서버의 이름
     * @return 생성된 서버의 고유 식별자 (UUID)
     * */
    UUID createServer(String name);

    /**
     * 사용자를 특정 서버에 가입시킵니다.
     *
     * @param userId 가입할 사용자 ID
     * @param name 가입할 서버 이름
     * @return 가입 성공 여부 (true = 가입 성공, false = 실패)
     */
    boolean joinServer(UUID userId, String name);

    /**
     * 특정 서버를 조회합니다.
     *
     * @param userId 조회할 사용자 ID
     * @param name 조회할 서버 이름
     * @return 조회된 서버 객체
     */
    Server getServer(UUID userId, String name);

    /**
     * 사용자가 속한 서버 정보를 출력합니다.
     *
     * @param userId 조회할 사용자 ID
     */
    void printServer(UUID userId);

    /**
     * 특정 서버를 삭제합니다.
     *
     * @param userId 삭제 요청 사용자 ID
     * @param targetName 삭제할 서버의 이름
     * @return 삭제 성공 여부 (true = 삭제 성공, false = 실패)
     */
    boolean removeServer(UUID userId, String targetName);

    /**
     * 서버 이름을 변경합니다.
     *
     * @param userId 요청 사용자 ID
     * @param targetName 변경할 대상 서버 이름
     * @param replaceName 변경할 새로운 이름
     * @return 변경 성공 여부 (true = 변경 성공, false = 실패)
     */
    boolean updateServer(UUID userId, String targetName, String replaceName);
}
