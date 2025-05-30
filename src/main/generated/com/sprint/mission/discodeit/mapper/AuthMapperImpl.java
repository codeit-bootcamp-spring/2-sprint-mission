package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.controller.auth.LoginRequestDTO;
import com.sprint.mission.discodeit.dto.controller.auth.LoginResponseDTO;
import com.sprint.mission.discodeit.dto.service.auth.LoginCommand;
import com.sprint.mission.discodeit.dto.service.auth.LoginResult;
import com.sprint.mission.discodeit.dto.service.binarycontent.FindBinaryContentResult;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-22T17:24:58+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class AuthMapperImpl implements AuthMapper {

    @Override
    public LoginCommand toLoginCommand(LoginRequestDTO loginRequestDTO) {
        if ( loginRequestDTO == null ) {
            return null;
        }

        String username = null;
        String password = null;

        username = loginRequestDTO.username();
        password = loginRequestDTO.password();

        LoginCommand loginCommand = new LoginCommand( username, password );

        return loginCommand;
    }

    @Override
    public LoginResponseDTO toLoginResponseDTO(LoginResult loginResult) {
        if ( loginResult == null ) {
            return null;
        }

        UUID id = null;
        String username = null;
        String email = null;
        Boolean online = null;

        id = loginResult.id();
        username = loginResult.username();
        email = loginResult.email();
        online = loginResult.online();

        UUID profileId = null;
        Instant createdAt = null;
        Instant updatedAt = null;

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO( id, profileId, createdAt, updatedAt, username, email, online );

        return loginResponseDTO;
    }

    @Override
    public LoginResult toLoginResult(User user) {
        if ( user == null ) {
            return null;
        }

        UUID id = null;
        FindBinaryContentResult profile = null;
        String username = null;
        String email = null;

        id = user.getId();
        profile = binaryContentToFindBinaryContentResult( user.getProfile() );
        username = user.getUsername();
        email = user.getEmail();

        Boolean online = null;

        LoginResult loginResult = new LoginResult( id, profile, username, email, online );

        return loginResult;
    }

    protected FindBinaryContentResult binaryContentToFindBinaryContentResult(BinaryContent binaryContent) {
        if ( binaryContent == null ) {
            return null;
        }

        UUID id = null;
        String filename = null;
        long size = 0L;
        String contentType = null;

        id = binaryContent.getId();
        filename = binaryContent.getFilename();
        size = binaryContent.getSize();
        contentType = binaryContent.getContentType();

        FindBinaryContentResult findBinaryContentResult = new FindBinaryContentResult( id, filename, size, contentType );

        return findBinaryContentResult;
    }
}
