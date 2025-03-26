package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UpdateUserReqDto;
import com.sprint.mission.discodeit.dto.user.CreateUserReqDto;
import com.sprint.mission.discodeit.dto.user.UserResDto;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResDto create(CreateUserReqDto createUserReqDto);
    UserResDto find(UUID userId);
    List<UserResDto> findAll();
    UserResDto update(UUID userId, UpdateUserReqDto updateUserReqDto);
    void delete(UUID userId);
}
