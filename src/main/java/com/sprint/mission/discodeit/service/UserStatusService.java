package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.UserStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * 사용자 상태를 관리하는 서비스 인터페이스입니다.
 * 사용자 상태를 생성, 조회, 수정 및 삭제하는 기능을 제공합니다.
 */
@Service
public interface UserStatusService {

    void create(String userId);

    UserStatus findByUserId(String userId);

    UserStatus findByStatusId(String userStatusId);

    List<UserStatus> findAll();

    void update(String userId, String replaceId);

    void delete(String userStatusId);
}
