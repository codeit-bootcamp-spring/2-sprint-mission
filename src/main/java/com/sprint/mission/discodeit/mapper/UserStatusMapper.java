package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.domain.UserStatus;
import com.sprint.mission.discodeit.dto.UserStatusDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserStatusMapper {

  UserStatusDto toDto(UserStatus userStatus);
}
