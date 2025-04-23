package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.service.user.userstatus.UserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserStatusMapper {

  @Mapping(source = "user.id", target = "userId")
  UserStatusDto toDto(UserStatus userStatus);

  @Mapping(source = "user.id", target = "userId")
  List<UserStatusDto> toDtoList(List<UserStatus> userStatus);
}
