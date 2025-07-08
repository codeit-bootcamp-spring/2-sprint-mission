package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BinaryContentMapper.class})
public interface UserMapper {

    List<UserDto> toDto(List<User> users);


    @Mapping(target = "profile", source = "profile")
    @Mapping(target = "online", constant = "false")
    UserDto toDto(User user);


    @Mapping(target = "online", source = "online")
    UserDto toDto(User user, Boolean online);
}
