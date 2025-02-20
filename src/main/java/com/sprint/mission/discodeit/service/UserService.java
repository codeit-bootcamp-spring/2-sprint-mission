package com.sprint.mission.discodeit.service;

import com.sprint.mission.application.UserDto;
import com.sprint.mission.application.UserRegisterDto;
import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDto findById(UUID id);

    List<UserDto> findByName(String name);

    List<UserDto> findAll();

    void register(UserRegisterDto userRegisterDto);
}
