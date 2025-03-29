package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.controller.auth.LoginRequestDTO;
import com.sprint.mission.discodeit.dto.controller.auth.LoginResponseDTO;
import com.sprint.mission.discodeit.dto.service.auth.LoginDTO;
import com.sprint.mission.discodeit.dto.service.auth.LoginParam;
import com.sprint.mission.discodeit.entity.User;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-29T13:04:09+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.14 (Oracle Corporation)"
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
    public LoginResponseDTO toLoginResponseDTO(LoginDTO loginDTO) {
        if ( loginDTO == null ) {
            return null;
        }

        String username = null;

        username = loginDTO.username();

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO( username );

        return loginResponseDTO;
    }

    @Override
    public LoginDTO toLoginDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UUID id = null;
        String username = null;

        id = user.getId();
        username = user.getUsername();

        LoginDTO loginDTO = new LoginDTO( id, username );

        return loginDTO;
    }
}
