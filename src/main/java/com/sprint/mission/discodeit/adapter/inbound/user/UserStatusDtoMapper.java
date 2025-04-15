package com.sprint.mission.discodeit.adapter.inbound.user;

import com.sprint.mission.discodeit.adapter.inbound.user.response.UserStatusResponse;
import com.sprint.mission.discodeit.core.status.usecase.user.dto.UserStatusResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserStatusDtoMapper {

  UserStatusResponse toCreateResponse(UserStatusResult userStatus);
}
