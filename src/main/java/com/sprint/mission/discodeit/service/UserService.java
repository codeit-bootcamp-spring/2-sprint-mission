package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.FindUserDto;
import com.sprint.mission.discodeit.dto.SaveUserParamDto;
import com.sprint.mission.discodeit.dto.UpdateUserParamDto;

import java.util.List;
import java.util.UUID;

public interface UserService {
    void save(SaveUserParamDto saveUserParamDto);
    FindUserDto findByUser(UUID uuid);
    List<FindUserDto> findAllUser();
    void update(UpdateUserParamDto updateUserDto);
    void delete(UUID uuid);
}
