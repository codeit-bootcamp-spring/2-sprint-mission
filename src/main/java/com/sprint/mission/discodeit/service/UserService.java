package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.service.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.service.dto.user.UserDto;
import com.sprint.mission.discodeit.service.dto.user.UserUpdateRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User create(UserCreateRequest createRequest,
                BinaryContentCreateRequest binaryData);

    UserDto find(UUID userId);

    List<UserDto> findAll();

    User update(UUID userId, UserUpdateRequest updateRequest, BinaryContentCreateRequest binaryData);

    void delete(UUID userId);

    Optional<User> findByUsername(String username);
}
