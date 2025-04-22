package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResult;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;

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
