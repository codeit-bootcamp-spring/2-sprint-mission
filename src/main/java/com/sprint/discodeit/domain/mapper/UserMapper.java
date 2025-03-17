package com.sprint.discodeit.domain.mapper;

import com.sprint.discodeit.domain.dto.UserNameResponse;
import com.sprint.discodeit.domain.dto.UserRequestDto;
import com.sprint.discodeit.domain.entity.User;
import java.util.UUID;

public class UserMapper {

    public static User toUserMapper(UserRequestDto userRequestDto, UUID profileId) {
        return User.builder()
                .email(userRequestDto.email())
                .password(userRequestDto.password())
                .username(userRequestDto.username())
                .profileId(profileId)
                .build();
    }

    public static UserNameResponse toUserNameResponse(String username) {
        return new UserNameResponse(username);
    }

}