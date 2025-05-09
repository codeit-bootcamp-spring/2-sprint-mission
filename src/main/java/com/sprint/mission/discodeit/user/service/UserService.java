package com.sprint.mission.discodeit.user.service;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.user.dto.UserResult;
import com.sprint.mission.discodeit.user.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.user.dto.user.UserUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResult register(UserCreateRequest userRequest, BinaryContentRequest binaryContentRequest);

    UserResult getById(UUID userId);

    UserResult getByName(String name);

    List<UserResult> getAll();

    UserResult getByEmail(String email);

    UserResult update(UUID userId, UserUpdateRequest userUpdateRequest, BinaryContentRequest binaryContentRequest);

    void delete(UUID userId);
}
