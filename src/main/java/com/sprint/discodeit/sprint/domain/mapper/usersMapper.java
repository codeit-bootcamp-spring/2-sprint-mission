package com.sprint.discodeit.sprint.domain.mapper;

import com.sprint.discodeit.sprint.domain.dto.usersDto.usersRequestDto;
import com.sprint.discodeit.sprint.domain.entity.users;

public class usersMapper {

    public static users tousers(usersRequestDto usersRequestDto) {
        return users.builder()
                .email(usersRequestDto.email())
                .password(usersRequestDto.password())
                .usersname(usersRequestDto.usersname())
                .build();
    }
}