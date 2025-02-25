package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User createUser(String name); // 사용자 생성
    Optional<User> getUserById(UUID userid); // 특정 사용자 조회
    List<User> getUsersByName(String name);  // 이름과 일치하는 사용자 조회
    List<User> getAllUsers(); // 모든 사용자 조회
    void updateUser(UUID userId, String newName); // 사용자 정보 수정
    void deleteUser(UUID userId);  // 사용자 삭제

}
