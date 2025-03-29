package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component//유저 CRUD
public interface UserService {

    UserDto.Summary findByUserId(UUID id);
    List<UserDto.Summary> findByAllUsersId();
    void deleteUser(UUID id);
    UserDto.Response createdUser(UserDto.Create createUserDto);
    //업데이트 권한에 대한 것은?
    UserDto.Update updateUser(UUID userId, UserDto.Update updateUserDto);

    boolean existsById(String userId);
}
