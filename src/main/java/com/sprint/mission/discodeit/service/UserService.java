package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.CreateDefinition;
import com.sprint.mission.discodeit.dto.UpdateDefinition;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User create(CreateDefinition createDefinition);
    User find(UUID userId);
    List<User> findAll();
    User update(UUID userId, UpdateDefinition updateDefinition);
    void delete(UUID userId);
}
