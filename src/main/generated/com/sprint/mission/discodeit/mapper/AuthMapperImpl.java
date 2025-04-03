package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.controller.auth.LoginRequestDTO;
import com.sprint.mission.discodeit.dto.controller.auth.LoginResponseDTO;
import com.sprint.mission.discodeit.dto.service.auth.LoginDTO;
import com.sprint.mission.discodeit.dto.service.auth.LoginParam;
import com.sprint.mission.discodeit.dto.service.binarycontent.BinaryContentDTO;
import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-03T20:22:36+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Oracle Corporation)"
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

        UUID id = null;
        Instant createdAt = null;
        Instant updatedAt = null;
        String username = null;
        String email = null;
        Boolean isLogin = null;

        id = loginDTO.id();
        createdAt = loginDTO.createdAt();
        updatedAt = loginDTO.updatedAt();
        username = loginDTO.username();
        email = loginDTO.email();
        isLogin = loginDTO.isLogin();

        BinaryContentDTO binaryContentDTO = null;

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO( id, binaryContentDTO, createdAt, updatedAt, username, email, isLogin );

        return loginResponseDTO;
    }

    @Override
    public LoginDTO toLoginDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UUID id = null;
        UUID profileId = null;
        Instant createdAt = null;
        Instant updatedAt = null;
        String username = null;
        String email = null;

        id = user.getId();
        profileId = user.getProfileId();
        createdAt = user.getCreatedAt();
        updatedAt = user.getUpdatedAt();
        username = user.getUsername();
        email = user.getEmail();

        Boolean isLogin = null;

        LoginDTO loginDTO = new LoginDTO( id, profileId, createdAt, updatedAt, username, email, isLogin );

        return loginDTO;
    }
}
