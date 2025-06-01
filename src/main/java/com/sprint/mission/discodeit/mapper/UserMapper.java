package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BinaryContentMapper.class})
public abstract class UserMapper {

    public abstract List<UserDto> toDto(List<User> users);

    @Mapping(target = "profile", source = "profile")
    @Mapping(
        target = "online",
        expression = "java(user.getUserStatus() != null && user.getUserStatus().isOnline())"
    )
    public abstract UserDto toDto(User user);
}
