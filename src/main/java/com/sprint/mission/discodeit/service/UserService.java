package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserCreateDto;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;

public interface UserService {
    User create(UserCreateDto userCreateDto);

    User findById(UUID userId);

    List<User> findAll();

    User update(UUID userId, String newUsername, String newEmail, String newPassword);

    void delete(UUID userId);
}
