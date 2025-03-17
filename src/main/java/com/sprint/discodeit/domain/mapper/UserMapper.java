package com.sprint.discodeit.domain.mapper;

import com.sprint.discodeit.domain.dto.UserRequestDto;
import com.sprint.discodeit.domain.entity.User;

public class UserMapper {

    public static User toUserMapper(UserRequestDto userRequestDto) {
        return User.builder()
                .email(userRequestDto.email())
                .password(userRequestDto.password())
                .username(userRequestDto.username())
                .profileId(userRequestDto.ImgUrl())
                .build();
    }
}
