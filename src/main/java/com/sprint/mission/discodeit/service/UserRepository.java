package com.sprint.mission.discodeit.service;
import com.sprint.mission.discodeit.entity.User;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserRepository {
     Optional<User> findByUser(UUID userId);//조회
     boolean register(User user);//저장
     boolean deleteUser(UUID userId); //삭제
     Set<UUID> findAllUsers(); //전체조회
     boolean updateUser(User user);
}
