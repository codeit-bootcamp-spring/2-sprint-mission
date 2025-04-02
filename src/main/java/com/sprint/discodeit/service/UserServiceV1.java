package com.sprint.discodeit.service;

import com.sprint.discodeit.domain.dto.userDto.UserNameStatusResponseDto;
import com.sprint.discodeit.domain.dto.userDto.UserProfileImgResponseDto;
import com.sprint.discodeit.domain.dto.userDto.UserRequestDto;
import com.sprint.discodeit.domain.dto.userDto.UserResponseDto;
import com.sprint.discodeit.domain.dto.userDto.UserUpdateRequestDto;
import com.sprint.discodeit.domain.entity.User;
import java.util.List;
import java.util.UUID;

public interface UserServiceV1 {

    UserNameStatusResponseDto create(UserRequestDto userRequestDto, UserProfileImgResponseDto userProfileImgResponseDto);
    UserResponseDto find(UUID userId);
    List<User> findAll();
    UserResponseDto update(UserUpdateRequestDto userUpdateRequestDto, String userId);
    void delete(UUID userId);
}
