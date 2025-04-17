package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.service.dto.request.binarycontentdto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.service.dto.request.userdto.UserCreateDto;
import com.sprint.mission.discodeit.service.dto.request.userdto.UserUpdateDto;
import com.sprint.mission.discodeit.service.dto.response.UserResponseDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UserService {
    UserResponseDto create(UserCreateDto userCreateDto, Optional<BinaryContentCreateDto> optionalBinaryContentCreateDto);
    UserResponseDto find(UUID userId);
    List<UserResponseDto> findAllUser();
    UserResponseDto update(UUID userId, UserUpdateDto userUpdateDto, Optional<BinaryContentCreateDto> optionalBinaryContentCreateDto);
    void delete(UUID userId);


}
