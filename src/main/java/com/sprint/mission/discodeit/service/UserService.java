package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.service.dto.user.UserInfoResponse;
import com.sprint.mission.discodeit.service.dto.user.UserUpdateRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User create(UserCreateRequest createParam);

    UserInfoResponse find(UUID userId);

    List<UserInfoResponse> findAll();

    User update(UserUpdateRequest updateParam);

    void delete(UUID userId);

    Optional<User> findByUsername(String username);
}
