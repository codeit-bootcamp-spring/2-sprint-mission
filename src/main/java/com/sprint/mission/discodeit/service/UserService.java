package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    void save(SaveUserRequestDto saveUserParamDto, Optional<SaveBinaryContentRequestDto> saveBinaryContentRequestDto);
    FindUserDto findByUser(UUID userId);
    List<FindUserDto> findAllUser();
    void update(UUID userId, UpdateUserRequestDto updateUserDto, Optional<SaveBinaryContentRequestDto> saveBinaryContentRequestDto);
    void delete(UUID userId);
}
