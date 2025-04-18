package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring", uses = {BinaryContentMapper.class})
public abstract class UserMapper {

    public abstract List<UserDto> toDto(List<User> users);

    @Mapping(target = "profile", source = "profile")
    @Mapping(target = "online", expression = "java(user.getUserStatus().isOnline())")
    public abstract UserDto toDto(User user);
}
