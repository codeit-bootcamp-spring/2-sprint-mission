package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.Map;
import java.util.UUID;

public interface UserService {
    void create(String userName);
    void delete(String userName);
    void update(String userName);
    User read(String userName);
    Map<UUID,User> readAll();
}
