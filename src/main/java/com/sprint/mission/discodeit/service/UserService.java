package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    void save(String name, String password, String nickname, String email, String path);
    User findByUser(UUID uuid);
    List<User> findAllUser();
    void update(UUID uuid, String nickname);
    void delete(UUID uuid);
}
