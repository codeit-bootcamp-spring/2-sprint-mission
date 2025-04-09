package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.controller.auth.LoginRequestDTO;
import com.sprint.mission.discodeit.dto.controller.auth.LoginResponseDTO;
import com.sprint.mission.discodeit.dto.service.auth.LoginDTO;
import com.sprint.mission.discodeit.dto.service.auth.LoginParam;
import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-04T10:27:12+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
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
        UUID profileId = null;
        Instant createdAt = null;
        Instant updatedAt = null;
        String username = null;
        String email = null;
        Boolean online = null;

        id = loginDTO.id();
        profileId = loginDTO.profileId();
        createdAt = loginDTO.createdAt();
        updatedAt = loginDTO.updatedAt();
        username = loginDTO.username();
        email = loginDTO.email();
        online = loginDTO.online();

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO( id, profileId, createdAt, updatedAt, username, email, online );

        return loginResponseDTO;
    }

    @Override
    public LoginDTO toLoginDTO(User user, boolean online) {
        if ( user == null ) {
            return null;
        }

        UUID id = null;
        UUID profileId = null;
        Instant createdAt = null;
        Instant updatedAt = null;
        String username = null;
        String email = null;
        if ( user != null ) {
            id = user.getId();
            profileId = user.getProfileId();
            createdAt = user.getCreatedAt();
            updatedAt = user.getUpdatedAt();
            username = user.getUsername();
            email = user.getEmail();
        }
        Boolean online1 = null;
        online1 = online;

        LoginDTO loginDTO = new LoginDTO( id, profileId, createdAt, updatedAt, username, email, online1 );

        return loginDTO;
    }
}
