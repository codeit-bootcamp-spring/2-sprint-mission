package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    void save(String nickname, String password);
    User findByUser(UUID uuid);
    Optional<List<User>> findAllUser();
    void update(UUID uuid, String nickname);
    void delete(UUID uuid);
    UUID login(UUID userUUID, String password);
}
