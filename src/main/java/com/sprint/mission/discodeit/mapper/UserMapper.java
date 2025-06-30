package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = BinaryContentMapper.class)
public interface UserMapper {

    UserDto toDto(User user);

    default UserDto toDtoWithOnline(User user, boolean online) {
        UserDto dto = toDto(user);
        return new UserDto(
                dto.id(),
                dto.username(),
                dto.email(),
                dto.profile(),
                online,
                dto.role()
        );
    }
}
