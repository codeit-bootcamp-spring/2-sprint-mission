package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.controller.auth.LoginRequestDTO;
import com.sprint.mission.discodeit.dto.controller.auth.LoginResponseDTO;
import com.sprint.mission.discodeit.dto.service.auth.LoginCommand;
import com.sprint.mission.discodeit.dto.service.auth.LoginResult;
import com.sprint.mission.discodeit.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AuthMapper {

  AuthMapper INSTANCE = Mappers.getMapper(AuthMapper.class);

  LoginCommand toLoginCommand(LoginRequestDTO loginRequestDTO);

  LoginResponseDTO toLoginResponseDTO(LoginResult loginResult);

  LoginResult toLoginResult(User user);
}
