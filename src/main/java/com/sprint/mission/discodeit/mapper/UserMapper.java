package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.controller.user.*;
import com.sprint.mission.discodeit.dto.service.binaryContent.CreateBinaryContentParam;
import com.sprint.mission.discodeit.dto.service.user.CreateUserParam;
import com.sprint.mission.discodeit.dto.service.user.UpdateUserParam;
import com.sprint.mission.discodeit.dto.service.user.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    CreateUserParam toCreateUserParam(CreateUserRequestDTO createUserRequestDTO);
    CreateUserResponseDTO toCreateUserResponseDTO(UserDTO userDTO);

    UserResponseDTO toUserResponseDTO(UserDTO userDTO);
    UpdateUserParam toUpdateUserParam(UpdateUserRequestDTO updateUserRequestDTO);

}
