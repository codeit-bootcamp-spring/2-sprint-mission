package com.sprint.mission.discodeit.service;

import com.sprint.mission.application.UserDto;
import com.sprint.mission.application.UserRegisterDto;
import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDto findById(UUID id);

    List<UserDto> findByName(String name);

    List<UserDto> findAll();

    UserDto register(UserRegisterDto userRegisterDto);

//    void updateNameById(UUID id, String name);
}
