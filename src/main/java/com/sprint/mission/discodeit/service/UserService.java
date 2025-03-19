package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.dto.userdto.UserCreateDto;
import com.sprint.mission.discodeit.service.dto.userdto.UserDeleteDto;
import com.sprint.mission.discodeit.service.dto.userdto.UserFindDto;
import com.sprint.mission.discodeit.service.dto.userdto.UserUpdateDto;

import java.util.List;


public interface UserService {
    User create(UserCreateDto userCreateDto);
    User getUser(UserFindDto userFindDto);
    List<User> getAllUser();
    User update(UserUpdateDto userUpdateDto);
    void delete(UserDeleteDto userDeleteDto);
}
