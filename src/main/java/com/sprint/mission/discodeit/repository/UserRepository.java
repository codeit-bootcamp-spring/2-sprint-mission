package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(UUID id);
    Optional<User> findByUsername(String username);
    List<User> findAll();
    boolean existsById(UUID id);
    void deleteById(UUID id);
    // 고도화 메서드 추가 - username과 email 중복 검사
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
//    Optional<User> loginAuth(String username, String password);
}
