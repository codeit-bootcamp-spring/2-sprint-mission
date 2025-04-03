package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.controller.user.*;
import com.sprint.mission.discodeit.dto.service.binarycontent.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.service.user.CreateUserParam;
import com.sprint.mission.discodeit.dto.service.user.UpdateUserDTO;
import com.sprint.mission.discodeit.dto.service.user.UpdateUserParam;
import com.sprint.mission.discodeit.dto.service.user.UserDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  CreateUserParam toCreateUserParam(CreateUserRequestDTO createUserRequestDTO);

  CreateUserResponseDTO toCreateUserResponseDTO(UserDTO userDTO);

  UpdateUserParam toUpdateUserParam(UpdateUserRequestDTO updateUserRequestDTO);

  UpdateUserResponseDTO toUpdateUserResponseDTO(UpdateUserDTO updateUserDTO);

  UserResponseDTO toUserResponseDTO(UserDTO userDTO);

  @Mapping(source = "user.id", target = "id")
  @Mapping(source = "user.createdAt", target = "createdAt")
  @Mapping(source = "user.updatedAt", target = "updatedAt")
  @Mapping(expression = "java(userStatus.isLoginUser())", target = "online")
  UserDTO toUserDTO(User user, UserStatus userStatus);

  @Mapping(source = "user.id", target = "id")
  UpdateUserDTO toUpdateUserDTO(User user, BinaryContentDTO binaryContentDTO);
}
