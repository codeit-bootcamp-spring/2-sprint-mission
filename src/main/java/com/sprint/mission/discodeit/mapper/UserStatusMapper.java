package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.controller.user.UpdateUserStatusResponseDTO;
import com.sprint.mission.discodeit.dto.service.userStatus.CreateUserStatusCommand;
import com.sprint.mission.discodeit.dto.service.userStatus.CreateUserStatusResult;
import com.sprint.mission.discodeit.dto.service.userStatus.FindUserStatusResult;
import com.sprint.mission.discodeit.dto.service.userStatus.UpdateUserStatusResult;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserStatusMapper {

  UserStatusMapper INSTANCE = Mappers.getMapper(UserStatusMapper.class);


  UpdateUserStatusResponseDTO toUpdateUserStatusResponseDTO(
      UpdateUserStatusResult updateUserStatusResult);

  @Mapping(target = "userId", source = "user.id")
  CreateUserStatusResult toCreateUserStatusResult(UserStatus userStatus);

  @Mapping(target = "userId", source = "user.id")
  FindUserStatusResult toFindUserStatusResult(UserStatus userStatus);

  @Mapping(target = "userId", source = "user.id")
  CreateUserStatusCommand toCreateUserStatusCommand(UserStatus userStatus);

  @Mapping(target = "userId", source = "user.id")
  UpdateUserStatusResult toUpdateUserStatusResult(UserStatus userStatus);

}
