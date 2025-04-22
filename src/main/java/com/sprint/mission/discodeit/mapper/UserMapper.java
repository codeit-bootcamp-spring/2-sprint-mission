package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.controller.user.*;
import com.sprint.mission.discodeit.dto.service.binarycontent.FindBinaryContentResult;
import com.sprint.mission.discodeit.dto.service.user.CreateUserCommand;
import com.sprint.mission.discodeit.dto.service.user.CreateUserResult;
import com.sprint.mission.discodeit.dto.service.user.FindUserResult;
import com.sprint.mission.discodeit.dto.service.user.UpdateUserCommand;
import com.sprint.mission.discodeit.dto.service.user.UpdateUserResult;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  CreateUserCommand toCreateUserCommand(CreateUserRequestDTO createUserRequestDTO);

  CreateUserResponseDTO toCreateUserResponseDTO(CreateUserResult createUserResult);

  UpdateUserCommand toUpdateUserCommand(UpdateUserRequestDTO updateUserRequestDTO);

  UpdateUserResponseDTO toUpdateUserResponseDTO(UpdateUserResult updateUserResult);

  FindUserResponseDTO toFindUserResponseDTO(FindUserResult findUserResult);
  
  @Mapping(target = "online", expression = "java(user.getUserStatus().isLoginUser())")
  FindUserResult toFindUserResult(User user);

  @Mapping(target = "online", expression = "java(user.getUserStatus().isLoginUser())")
  CreateUserResult toCreateUserResult(User user);

  @Mapping(target = "online", expression = "java(user.getUserStatus().isLoginUser())")
  UpdateUserResult toUpdateUserResult(User user);

  FindBinaryContentResult toFindBinaryContentResult(BinaryContent binaryContent);

}
