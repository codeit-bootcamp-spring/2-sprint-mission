package com.sprint.discodeit.sprint5.domain.mapper;

import com.sprint.discodeit.sprint5.domain.dto.userDto.UserRequestDto;
import com.sprint.discodeit.sprint5.domain.entity.User;

public class UserMapper {

    public static User toUser(UserRequestDto userRequestDto) {
        return User.builder()
                .email(userRequestDto.email())
                .password(userRequestDto.password())
                .username(userRequestDto.username())
                .build();
    }
}