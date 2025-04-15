package com.sprint.mission.discodeit.adapter.inbound.user;

import com.sprint.mission.discodeit.adapter.inbound.content.BinaryContentDtoMapper;
import com.sprint.mission.discodeit.adapter.inbound.user.request.UserCreateRequest;
import com.sprint.mission.discodeit.adapter.inbound.user.request.UserLoginRequest;
import com.sprint.mission.discodeit.adapter.inbound.user.request.UserUpdateRequest;
import com.sprint.mission.discodeit.adapter.inbound.user.response.UserResponse;
import com.sprint.mission.discodeit.core.user.usecase.dto.CreateUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.LoginUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UpdateUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserResult;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BinaryContentDtoMapper.class})
public interface UserDtoMapper {

  UserResponse toCreateResponse(UserResult result);

  CreateUserCommand toCreateUserCommand(UserCreateRequest requestBody);

  LoginUserCommand toLoginUserCommand(UserLoginRequest requestBody);

  @Mapping(target = "requestUserId", source = "userId")
  UpdateUserCommand toUpdateUserCommand(UUID userId, UserUpdateRequest requestBody);

}
