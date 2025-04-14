package com.sprint.discodeit.sprint.domain.mapper;

import com.sprint.discodeit.sprint.domain.dto.usersDto.usersRequestDto;
import com.sprint.discodeit.sprint.domain.entity.Users;

public class usersMapper {

    public static Users tousers(usersRequestDto usersRequestDto) {
        return Users.builder()
                .email(usersRequestDto.email())
                .password(usersRequestDto.password())
                .username(usersRequestDto.usersname())
                .build();
    }
}