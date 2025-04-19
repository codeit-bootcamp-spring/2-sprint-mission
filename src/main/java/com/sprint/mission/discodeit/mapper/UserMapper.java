package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {BinaryContentMapper.class})
public interface UserMapper {

  UserDto toDto(User user, boolean online);
}
