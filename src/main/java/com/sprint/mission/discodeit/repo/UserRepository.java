package com.sprint.mission.discodeit.repo;

import com.sprint.mission.application.UserDto;
import com.sprint.mission.application.UserRegisterDto;
import java.util.List;
import java.util.UUID;

public interface UserRepository {
    UserDto register(UserRegisterDto userRegisterDto);

    UserDto findById(UUID id);

    List<UserDto> findByName(String name);

    List<UserDto> findAll();

    void updateName(UUID id, String name);

    void delete(UUID id);
}
