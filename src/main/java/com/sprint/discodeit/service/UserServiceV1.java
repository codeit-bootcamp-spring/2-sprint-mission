package com.sprint.discodeit.service;

import com.sprint.discodeit.domain.dto.UserNameResponse;
import com.sprint.discodeit.domain.dto.UserRequestDto;
import com.sprint.discodeit.domain.entity.User;
import java.util.List;
import java.util.UUID;

public interface UserServiceV1 {

    UserNameResponse create(UserRequestDto userRequestDto);
    User find(UUID userId);
    List<User> findAll();
    User update(UUID userId, String newUsername, String newEmail, String newPassword);
    void delete(UUID userId);
}
