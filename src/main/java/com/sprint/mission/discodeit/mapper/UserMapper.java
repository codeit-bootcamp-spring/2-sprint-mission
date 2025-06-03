package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BinaryContentMapper.class, UserStatusMapper.class})
public interface UserMapper {
  
  @Mapping(target = "online", expression = "java(user.getStatus().isOnline())")
  UserDto toDto(User user);
}
