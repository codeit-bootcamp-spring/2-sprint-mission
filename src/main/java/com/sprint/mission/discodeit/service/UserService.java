package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.application.UserRegisterDto;
import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDto register(UserRegisterDto userRegisterDto);

    UserDto findById(UUID id);

    List<UserDto> findByName(String name);

    List<UserDto> findAll();

    void updateName(UUID id, String name);

    void delete(UUID id);
}
