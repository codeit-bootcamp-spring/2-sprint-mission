package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.dto.UserResponseDto;
import java.util.List;
import java.util.UUID;


public interface UserService {
    User create(UserCreateRequest userRequest, ProfileImageRequest profileRequest);
    UserResponseDto find(UUID userId);
    List<UserResponseDto> findAll();
    User update(UserUpdateRequest updateRequest);
    void delete(UUID userId);
}
