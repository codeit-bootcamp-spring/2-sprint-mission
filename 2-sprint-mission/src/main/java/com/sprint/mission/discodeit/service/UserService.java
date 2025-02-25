package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserRole;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    void createUser(User user);
    Optional<User> selectUserById(UUID id);
    Optional<User> selectUserByNickname(String nickname);
    Optional<User> selectUserByEmail(String username);
    List<User> selectAllUsers();
    void updateUser(UUID id, String password, String nickname, UserStatus status, UserRole role);
    void deleteUser(UUID id);
}
