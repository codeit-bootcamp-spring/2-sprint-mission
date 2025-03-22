package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.userdto.UserDto;
import com.sprint.mission.discodeit.application.userdto.UserRegisterDto;

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

    UserDto updateProfileImage(UUID userId, UUID profileId);

    void delete(UUID userId);
}
