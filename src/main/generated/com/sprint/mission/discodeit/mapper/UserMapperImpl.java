package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.controller.user.CreateUserRequestDTO;
import com.sprint.mission.discodeit.dto.controller.user.CreateUserResponseDTO;
import com.sprint.mission.discodeit.dto.controller.user.UpdateUserRequestDTO;
import com.sprint.mission.discodeit.dto.controller.user.UpdateUserResponseDTO;
import com.sprint.mission.discodeit.dto.controller.user.UserResponseDTO;
import com.sprint.mission.discodeit.dto.service.binarycontent.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.service.user.CreateUserParam;
import com.sprint.mission.discodeit.dto.service.user.UpdateUserDTO;
import com.sprint.mission.discodeit.dto.service.user.UpdateUserParam;
import com.sprint.mission.discodeit.dto.service.user.UserDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
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
public class UserMapperImpl implements UserMapper {

    @Override
    public CreateUserParam toCreateUserParam(CreateUserRequestDTO createUserRequestDTO) {
        if ( createUserRequestDTO == null ) {
            return null;
        }

        String username = null;
        String email = null;
        String password = null;

        username = createUserRequestDTO.username();
        email = createUserRequestDTO.email();
        password = createUserRequestDTO.password();

        CreateUserParam createUserParam = new CreateUserParam( username, email, password );

        return createUserParam;
    }

    @Override
    public CreateUserResponseDTO toCreateUserResponseDTO(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        UUID id = null;
        UUID profileId = null;
        Instant createdAt = null;
        Instant updatedAt = null;
        String username = null;
        String email = null;
        Boolean online = null;

        id = userDTO.id();
        profileId = userDTO.profileId();
        createdAt = userDTO.createdAt();
        updatedAt = userDTO.updatedAt();
        username = userDTO.username();
        email = userDTO.email();
        online = userDTO.online();

        CreateUserResponseDTO createUserResponseDTO = new CreateUserResponseDTO( id, profileId, createdAt, updatedAt, username, email, online );

        return createUserResponseDTO;
    }

    @Override
    public UpdateUserParam toUpdateUserParam(UpdateUserRequestDTO updateUserRequestDTO) {
        if ( updateUserRequestDTO == null ) {
            return null;
        }

        String newUsername = null;
        String newEmail = null;
        String newPassword = null;

        newUsername = updateUserRequestDTO.newUsername();
        newEmail = updateUserRequestDTO.newEmail();
        newPassword = updateUserRequestDTO.newPassword();

        UpdateUserParam updateUserParam = new UpdateUserParam( newUsername, newEmail, newPassword );

        return updateUserParam;
    }

    @Override
    public UpdateUserResponseDTO toUpdateUserResponseDTO(UpdateUserDTO updateUserDTO) {
        if ( updateUserDTO == null ) {
            return null;
        }

        UUID id = null;
        UUID profileId = null;
        Instant createdAt = null;
        Instant updatedAt = null;
        String username = null;
        String email = null;
        Boolean online = null;

        id = updateUserDTO.id();
        profileId = updateUserDTO.profileId();
        createdAt = updateUserDTO.createdAt();
        updatedAt = updateUserDTO.updatedAt();
        username = updateUserDTO.username();
        email = updateUserDTO.email();
        online = updateUserDTO.online();

        UpdateUserResponseDTO updateUserResponseDTO = new UpdateUserResponseDTO( id, profileId, createdAt, updatedAt, username, email, online );

        return updateUserResponseDTO;
    }

    @Override
    public UserResponseDTO toUserResponseDTO(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        UUID id = null;
        UUID profileId = null;
        Instant createdAt = null;
        Instant updatedAt = null;
        String username = null;
        String email = null;
        Boolean online = null;

        id = userDTO.id();
        profileId = userDTO.profileId();
        createdAt = userDTO.createdAt();
        updatedAt = userDTO.updatedAt();
        username = userDTO.username();
        email = userDTO.email();
        online = userDTO.online();

        UserResponseDTO userResponseDTO = new UserResponseDTO( id, profileId, createdAt, updatedAt, username, email, online );

        return userResponseDTO;
    }

    @Override
    public UserDTO toUserDTO(User user, UserStatus userStatus) {
        if ( user == null && userStatus == null ) {
            return null;
        }

        UUID id = null;
        Instant createdAt = null;
        Instant updatedAt = null;
        UUID profileId = null;
        String username = null;
        String email = null;
        if ( user != null ) {
            id = user.getId();
            createdAt = user.getCreatedAt();
            updatedAt = user.getUpdatedAt();
            profileId = user.getProfileId();
            username = user.getUsername();
            email = user.getEmail();
        }

        Boolean online = userStatus.isLoginUser();

        UserDTO userDTO = new UserDTO( id, profileId, createdAt, updatedAt, username, email, online );

        return userDTO;
    }

    @Override
    public UpdateUserDTO toUpdateUserDTO(User user, boolean online, BinaryContentDTO binaryContentDTO) {
        if ( user == null && binaryContentDTO == null ) {
            return null;
        }

        UUID id = null;
        Instant createdAt = null;
        Instant updatedAt = null;
        UUID profileId = null;
        String username = null;
        String email = null;
        if ( user != null ) {
            id = user.getId();
            createdAt = user.getCreatedAt();
            updatedAt = user.getUpdatedAt();
            profileId = user.getProfileId();
            username = user.getUsername();
            email = user.getEmail();
        }
        Boolean online1 = null;
        online1 = online;

        UpdateUserDTO updateUserDTO = new UpdateUserDTO( id, profileId, createdAt, updatedAt, username, email, online1 );

        return updateUserDTO;
    }
}
