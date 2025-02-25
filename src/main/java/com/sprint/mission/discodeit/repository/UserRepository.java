package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository {
    User createUser(String nickname, String email, String avatar, String status);
    User getUserById(UUID userId);
    List<User> getUsers();
    User updateUser(UUID userId, String nickname, String avatar, String status);

}
