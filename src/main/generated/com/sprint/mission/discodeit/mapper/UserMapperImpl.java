package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.controller.user.CreateUserRequestDTO;
import com.sprint.mission.discodeit.dto.controller.user.CreateUserResponseDTO;
import com.sprint.mission.discodeit.dto.controller.user.FindUserResponseDTO;
import com.sprint.mission.discodeit.dto.controller.user.UpdateUserRequestDTO;
import com.sprint.mission.discodeit.dto.controller.user.UpdateUserResponseDTO;
import com.sprint.mission.discodeit.dto.service.binarycontent.FindBinaryContentResult;
import com.sprint.mission.discodeit.dto.service.user.CreateUserCommand;
import com.sprint.mission.discodeit.dto.service.user.CreateUserResult;
import com.sprint.mission.discodeit.dto.service.user.FindUserResult;
import com.sprint.mission.discodeit.dto.service.user.UpdateUserCommand;
import com.sprint.mission.discodeit.dto.service.user.UpdateUserResult;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-22T17:24:59+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public CreateUserCommand toCreateUserCommand(CreateUserRequestDTO createUserRequestDTO) {
        if ( createUserRequestDTO == null ) {
            return null;
        }

        String username = null;
        String email = null;
        String password = null;

        username = createUserRequestDTO.username();
        email = createUserRequestDTO.email();
        password = createUserRequestDTO.password();

        CreateUserCommand createUserCommand = new CreateUserCommand( username, email, password );

        return createUserCommand;
    }

    @Override
    public CreateUserResponseDTO toCreateUserResponseDTO(CreateUserResult createUserResult) {
        if ( createUserResult == null ) {
            return null;
        }

        UUID id = null;
        FindBinaryContentResult profile = null;
        String username = null;
        String email = null;
        Boolean online = null;

        id = createUserResult.id();
        profile = createUserResult.profile();
        username = createUserResult.username();
        email = createUserResult.email();
        online = createUserResult.online();

        CreateUserResponseDTO createUserResponseDTO = new CreateUserResponseDTO( id, profile, username, email, online );

        return createUserResponseDTO;
    }

    @Override
    public UpdateUserCommand toUpdateUserCommand(UpdateUserRequestDTO updateUserRequestDTO) {
        if ( updateUserRequestDTO == null ) {
            return null;
        }

        String newUsername = null;
        String newEmail = null;
        String newPassword = null;

        newUsername = updateUserRequestDTO.newUsername();
        newEmail = updateUserRequestDTO.newEmail();
        newPassword = updateUserRequestDTO.newPassword();

        UpdateUserCommand updateUserCommand = new UpdateUserCommand( newUsername, newEmail, newPassword );

        return updateUserCommand;
    }

    @Override
    public UpdateUserResponseDTO toUpdateUserResponseDTO(UpdateUserResult updateUserResult) {
        if ( updateUserResult == null ) {
            return null;
        }

        UUID id = null;
        FindBinaryContentResult profile = null;
        String username = null;
        String email = null;
        Boolean online = null;

        id = updateUserResult.id();
        profile = updateUserResult.profile();
        username = updateUserResult.username();
        email = updateUserResult.email();
        online = updateUserResult.online();

        UpdateUserResponseDTO updateUserResponseDTO = new UpdateUserResponseDTO( id, profile, username, email, online );

        return updateUserResponseDTO;
    }

    @Override
    public FindUserResponseDTO toFindUserResponseDTO(FindUserResult findUserResult) {
        if ( findUserResult == null ) {
            return null;
        }

        UUID id = null;
        FindBinaryContentResult profile = null;
        String username = null;
        String email = null;
        Boolean online = null;

        id = findUserResult.id();
        profile = findUserResult.profile();
        username = findUserResult.username();
        email = findUserResult.email();
        online = findUserResult.online();

        FindUserResponseDTO findUserResponseDTO = new FindUserResponseDTO( id, profile, username, email, online );

        return findUserResponseDTO;
    }

    @Override
    public FindUserResult toFindUserResult(User user) {
        if ( user == null ) {
            return null;
        }

        UUID id = null;
        FindBinaryContentResult profile = null;
        String username = null;
        String email = null;

        id = user.getId();
        profile = toFindBinaryContentResult( user.getProfile() );
        username = user.getUsername();
        email = user.getEmail();

        Boolean online = user.getUserStatus().isLoginUser();

        FindUserResult findUserResult = new FindUserResult( id, profile, username, email, online );

        return findUserResult;
    }

    @Override
    public CreateUserResult toCreateUserResult(User user) {
        if ( user == null ) {
            return null;
        }

        UUID id = null;
        FindBinaryContentResult profile = null;
        String username = null;
        String email = null;

        id = user.getId();
        profile = toFindBinaryContentResult( user.getProfile() );
        username = user.getUsername();
        email = user.getEmail();

        Boolean online = user.getUserStatus().isLoginUser();

        CreateUserResult createUserResult = new CreateUserResult( id, profile, username, email, online );

        return createUserResult;
    }

    @Override
    public UpdateUserResult toUpdateUserResult(User user) {
        if ( user == null ) {
            return null;
        }

        UUID id = null;
        Instant updatedAt = null;
        FindBinaryContentResult profile = null;
        String username = null;
        String email = null;

        id = user.getId();
        updatedAt = user.getUpdatedAt();
        profile = toFindBinaryContentResult( user.getProfile() );
        username = user.getUsername();
        email = user.getEmail();

        Boolean online = user.getUserStatus().isLoginUser();

        UpdateUserResult updateUserResult = new UpdateUserResult( id, updatedAt, profile, username, email, online );

        return updateUserResult;
    }

    @Override
    public FindBinaryContentResult toFindBinaryContentResult(BinaryContent binaryContent) {
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
