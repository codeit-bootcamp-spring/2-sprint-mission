package com.sprint.discodeit.service;

import com.sprint.discodeit.domain.dto.UserNameStatusResponse;
import com.sprint.discodeit.domain.dto.UserProfileImgResponseDto;
import com.sprint.discodeit.domain.dto.UserRequestDto;
import com.sprint.discodeit.domain.dto.UserResponse;
import com.sprint.discodeit.domain.dto.UserUpdateRequest;
import com.sprint.discodeit.domain.entity.User;
import java.util.List;
import java.util.UUID;

public interface UserServiceV1 {

    UserNameStatusResponse create(UserRequestDto userRequestDto, UserProfileImgResponseDto userProfileImgResponseDto);
    UserResponse find(UUID userId);
    List<User> findAll();
    UserResponse update(UserUpdateRequest userUpdateRequest);
    void delete(UUID userId);
}
