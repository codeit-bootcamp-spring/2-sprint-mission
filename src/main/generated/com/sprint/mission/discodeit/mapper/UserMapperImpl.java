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
    date = "2025-03-28T22:33:57+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
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
        BinaryContentDTO binaryContentDTO = null;
        String username = null;
        String email = null;

        id = userDTO.id();
        binaryContentDTO = userDTO.binaryContentDTO();
        username = userDTO.username();
        email = userDTO.email();

        CreateUserResponseDTO createUserResponseDTO = new CreateUserResponseDTO( id, binaryContentDTO, username, email );

        return createUserResponseDTO;
    }

    @Override
    public UpdateUserParam toUpdateUserParam(UpdateUserRequestDTO updateUserRequestDTO) {
        if ( updateUserRequestDTO == null ) {
            return null;
        }

        String username = null;
        String email = null;
        String password = null;

        username = updateUserRequestDTO.username();
        email = updateUserRequestDTO.email();
        password = updateUserRequestDTO.password();

        UpdateUserParam updateUserParam = new UpdateUserParam( username, email, password );

        return updateUserParam;
    }

    @Override
    public UpdateUserResponseDTO toUpdateUserResponseDTO(UpdateUserDTO updateUserDTO) {
        if ( updateUserDTO == null ) {
            return null;
        }

        UUID id = null;
        BinaryContentDTO binaryContentDTO = null;
        Instant updatedAt = null;
        String username = null;
        String email = null;

        id = updateUserDTO.id();
        binaryContentDTO = updateUserDTO.binaryContentDTO();
        updatedAt = updateUserDTO.updatedAt();
        username = updateUserDTO.username();
        email = updateUserDTO.email();

        UpdateUserResponseDTO updateUserResponseDTO = new UpdateUserResponseDTO( id, binaryContentDTO, updatedAt, username, email );

        return updateUserResponseDTO;
    }

    @Override
    public UserResponseDTO toUserResponseDTO(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        UUID id = null;
        BinaryContentDTO binaryContentDTO = null;
        String username = null;
        String email = null;
        Instant createdAt = null;
        boolean isLogin = false;

        id = userDTO.id();
        binaryContentDTO = userDTO.binaryContentDTO();
        username = userDTO.username();
        email = userDTO.email();
        createdAt = userDTO.createdAt();
        if ( userDTO.isLogin() != null ) {
            isLogin = userDTO.isLogin();
        }

        UserResponseDTO userResponseDTO = new UserResponseDTO( id, binaryContentDTO, username, email, createdAt, isLogin );

        return userResponseDTO;
    }

    @Override
    public UserDTO toUserDTO(User user, UserStatus userStatus, BinaryContentDTO binaryContentDTO) {
        if ( user == null && userStatus == null && binaryContentDTO == null ) {
            return null;
        }

        UUID id = null;
        Instant createdAt = null;
        Instant updatedAt = null;
        String username = null;
        String email = null;
        if ( user != null ) {
            id = user.getId();
            createdAt = user.getCreatedAt();
            updatedAt = user.getUpdatedAt();
            username = user.getUsername();
            email = user.getEmail();
        }
        BinaryContentDTO binaryContentDTO1 = null;
        if ( binaryContentDTO != null ) {
            binaryContentDTO1 = binaryContentDTO;
        }

        Boolean isLogin = userStatus.isLoginUser();

        UserDTO userDTO = new UserDTO( id, binaryContentDTO1, createdAt, updatedAt, username, email, isLogin );

        return userDTO;
    }
}
