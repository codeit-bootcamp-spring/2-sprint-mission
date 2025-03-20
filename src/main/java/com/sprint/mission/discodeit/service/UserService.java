package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.application.UserRegisterDto;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDto register(UserRegisterDto userRegisterDto, UUID profileId);

    UserDto findById(UUID userId);

    UserDto findByName(String name);

    List<UserDto> findAll();

    UserDto findByEmail(String email);

    List<UserDto> findAllByIds(List<UUID> userIds);

    void updateName(UUID userId, String name);

    void delete(UUID userId);
}
