package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.controller.auth.LoginRequestDTO;
import com.sprint.mission.discodeit.dto.controller.auth.LoginResponseDTO;
import com.sprint.mission.discodeit.dto.service.auth.LoginParam;
import com.sprint.mission.discodeit.dto.service.user.UserDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-28T02:43:44+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class AuthMapperImpl implements AuthMapper {

    @Override
    public LoginParam toLoginParam(LoginRequestDTO loginRequestDTO) {
        if ( loginRequestDTO == null ) {
            return null;
        }

        String username = null;
        String password = null;

        username = loginRequestDTO.username();
        password = loginRequestDTO.password();

        LoginParam loginParam = new LoginParam( username, password );

        return loginParam;
    }

    @Override
    public LoginResponseDTO toLoginResponseDTO(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        String username = null;

        username = userDTO.username();

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO( username );

        return loginResponseDTO;
    }
}
