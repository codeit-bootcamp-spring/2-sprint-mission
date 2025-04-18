package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.service.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BinaryContentMapper.class})
public interface UserMapper {

  @Mapping(target = "online", expression = "java(user.getUserStatus().isOnline())")
  UserDto toDto(User user);

  @Mapping(target = "online", expression = "java(user.getUserStatus().isOnline())")
  List<UserDto> toDtoList(List<User> user);

}
