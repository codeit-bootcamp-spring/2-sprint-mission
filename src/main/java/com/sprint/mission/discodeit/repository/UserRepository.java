package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public interface UserRepository extends Repository<User> {
    void updateUserName(UUID userId, String newUserName);
    void updatePassword(UUID userId, String newPassword);
}
