package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.controller.user.*;
import com.sprint.mission.discodeit.dto.service.user.CreateUserParam;
import com.sprint.mission.discodeit.dto.service.user.UpdateUserDTO;
import com.sprint.mission.discodeit.dto.service.user.UpdateUserParam;
import com.sprint.mission.discodeit.dto.service.user.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    CreateUserParam toCreateUserParam(CreateUserRequestDTO createUserRequestDTO);
    CreateUserResponseDTO toCreateUserResponseDTO(UserDTO userDTO);

    UpdateUserParam toUpdateUserParam(UpdateUserRequestDTO updateUserRequestDTO);
    UpdateUserResponseDTO toUpdateUserResponseDTO(UpdateUserDTO updateUserDTO);

    UserResponseDTO toUserResponseDTO(UserDTO userDTO);

}
