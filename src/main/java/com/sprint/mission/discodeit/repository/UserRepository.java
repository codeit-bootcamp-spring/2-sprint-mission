package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(String username, String password, String nickname, String email, UUID profileId);
    Optional<User> findUserById(UUID userUUID);
    Optional<User> findUserByUsername(String username);
    Optional<User> findUserByEmail(String email);
    List<User> findAllUser();
    User update(UUID userUUID, String nickname, UUID profileId);
    boolean deleteUserById(UUID userUUID);
}
