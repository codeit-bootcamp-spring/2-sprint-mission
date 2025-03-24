package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserRole;
import com.sprint.mission.discodeit.entity.UserStatusType;

import java.util.List;
import java.util.UUID;

public interface UserRepository {
    UUID createUser(User user);
    User findById(UUID id);
    User findByNickname(String nickname);
    User findByEmail(String email);
    List<User> findAll();
    void updateUser(UUID id, String password, String nickname, UserStatusType status, UserRole role, UUID profileId);
    void deleteUser(UUID id);
}
