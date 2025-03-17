package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.service.CreateUserParam;
import com.sprint.mission.discodeit.dto.service.UpdateUserParam;
import com.sprint.mission.discodeit.dto.service.UserDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDTO create(CreateUserParam createUserParam);
    UserDTO find(UUID userId);
    List<UserDTO> findAll();
    UUID update(UUID userId, UpdateUserParam updateUserParam);
    void delete(UUID userId);;
}
