package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

// interface(인터페이스)
// 하위모듈에서 어떤 기능들을 구현할지 선언만 해놓은 공간
// CRUD(생성, 읽기, 수정, 삭제)
public interface UserService {
    // Create - 생성
    void createUser(String name);

    // Read - 읽기, 조회
    // user들을 list형태로 관리한다
    // 모든 user 조회(다건)
    List<User> getAllUser();
    // 특정 user 조회(단건)
    // optional은 null값 반환 시 오류가 나지 않도록 해준다.
    Optional<User> getOneUser(UUID id);

    // Update - 수정
    void updateUser(String newName, UUID id);

    // Delete - 삭제
    void deleteUser(UUID id);
}
