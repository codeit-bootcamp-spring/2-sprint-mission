package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(String nickname, String password);
    Optional<User> findUserById(UUID userUUID);
    List<User> findAllUser();
    User updateUserNickname(UUID userUUID, String nickname);
    boolean deleteUserById(UUID userUUID);
}
