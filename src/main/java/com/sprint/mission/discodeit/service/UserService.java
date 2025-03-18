package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ProfileImageRequest;
import com.sprint.mission.discodeit.dto.UpdateDefinition;
import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.dto.UserResponseDto;
import java.util.List;
import java.util.UUID;


public interface UserService {
    User create(UserCreateRequest userRequest, ProfileImageRequest profileRequest);
    UserResponseDto find(UUID userId);
    List<UserResponseDto> findAll();
    User update(UUID userId, UpdateDefinition updateDefinition, UUID newProfileId);
    void delete(UUID userId);
}
