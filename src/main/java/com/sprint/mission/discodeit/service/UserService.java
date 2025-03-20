package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.dto.userdto.*;

import java.util.List;


public interface UserService {
    User create(UserCreateDto userCreateDto);
    UserFindResponseDto find(UserFindRequestDto userFindRequestDto);
    List<UserFindAllResponseDto> findAllUser();
    User update(UserUpdateDto userUpdateDto);
    void delete(UserDeleteDto userDeleteDto);
}
