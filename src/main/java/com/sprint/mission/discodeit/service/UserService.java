package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UserUpdateDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User create(UserDto userDto, Optional<BinaryContentDto> userProfile);
    UserDto findById(UUID userId);
    List<UserDto> findAll();
    User update(UUID userId, UserUpdateDto userUpdateDto, Optional<BinaryContentDto> userProfile);
    void delete(UUID userId);
}