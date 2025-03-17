package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.UserStatus;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * 사용자 상태를 관리하는 서비스 인터페이스입니다.
 * 사용자 상태를 생성, 조회, 수정 및 삭제하는 기능을 제공합니다.
 */
@Service
public interface UserStatusService {

    /**
     * 지정된 사용자 ID에 대한 사용자 상태를 생성합니다.
     *
     * @param userId 생성할 사용자의 ID
     */
    void create(String userId);
    /**
     * 특정 사용자 ID에 대한 사용자 상태를 조회합니다.
     *
     * @param userId 조회할 사용자의 ID
     * @return 조회된 사용자 상태 객체, 존재하지 않을 경우 {@code null} 반환 가능
     */
    UserStatus find(String userId);

    /**
     * 모든 사용자 상태를 조회합니다.
     *
     * @return 사용자 상태 목록
     */
    List<UserStatus> findAll();
    /**
     * 특정 사용자 ID의 상태를 변경합니다.
     *
     * @param userId     변경할 대상 사용자의 ID
     * @param replaceId  새로운 사용자 ID 또는 상태를 나타내는 값
     */
    void update(String userId, String replaceId);
    /**
     * 특정 사용자 ID에 대한 사용자 상태를 삭제합니다.
     *
     * @param userId 삭제할 사용자의 ID
     */
    void delete(String userId);
}
