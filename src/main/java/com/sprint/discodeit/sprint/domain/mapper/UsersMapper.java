package com.sprint.discodeit.sprint.domain.mapper;

import com.sprint.discodeit.sprint.domain.dto.usersDto.UsersRequestDto;
import com.sprint.discodeit.sprint.domain.entity.Users;

public class UsersMapper {

    public static Users tousers(UsersRequestDto usersRequestDto) {
        return Users.builder()
                .email(usersRequestDto.email())
                .password(usersRequestDto.password())
                .username(usersRequestDto.usersname())
                .build();
    }
}