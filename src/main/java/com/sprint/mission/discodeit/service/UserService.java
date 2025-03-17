package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.FindUserDto;
import com.sprint.mission.discodeit.dto.UpdateUserDto;
import com.sprint.mission.discodeit.dto.UserSaveDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    UserSaveDto save(String name, String password, String nickname, String email, byte[] path);
    FindUserDto findByUser(UUID uuid);
    List<FindUserDto> findAllUser();
    void update(UpdateUserDto updateUserDto);
    void delete(UUID uuid);
}
