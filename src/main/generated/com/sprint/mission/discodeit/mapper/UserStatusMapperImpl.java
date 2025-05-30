package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.controller.user.UpdateUserStatusResponseDTO;
import com.sprint.mission.discodeit.dto.service.userStatus.CreateUserStatusCommand;
import com.sprint.mission.discodeit.dto.service.userStatus.CreateUserStatusResult;
import com.sprint.mission.discodeit.dto.service.userStatus.FindUserStatusResult;
import com.sprint.mission.discodeit.dto.service.userStatus.UpdateUserStatusResult;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
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
public class UserStatusMapperImpl implements UserStatusMapper {

    @Override
    public UpdateUserStatusResponseDTO toUpdateUserStatusResponseDTO(UpdateUserStatusResult updateUserStatusResult) {
        if ( updateUserStatusResult == null ) {
            return null;
        }

        UUID id = null;
        UUID userId = null;
        Instant lastActiveAt = null;

        id = updateUserStatusResult.id();
        userId = updateUserStatusResult.userId();
        lastActiveAt = updateUserStatusResult.lastActiveAt();

        UpdateUserStatusResponseDTO updateUserStatusResponseDTO = new UpdateUserStatusResponseDTO( id, userId, lastActiveAt );

        return updateUserStatusResponseDTO;
    }

    @Override
    public CreateUserStatusResult toCreateUserStatusResult(UserStatus userStatus) {
        if ( userStatus == null ) {
            return null;
        }

        UUID userId = null;
        UUID id = null;
        Instant lastActiveAt = null;

        userId = userStatusUserId( userStatus );
        id = userStatus.getId();
        lastActiveAt = userStatus.getLastActiveAt();

        CreateUserStatusResult createUserStatusResult = new CreateUserStatusResult( id, userId, lastActiveAt );

        return createUserStatusResult;
    }

    @Override
    public FindUserStatusResult toFindUserStatusResult(UserStatus userStatus) {
        if ( userStatus == null ) {
            return null;
        }

        UUID userId = null;
        UUID id = null;
        Instant lastActiveAt = null;

        userId = userStatusUserId( userStatus );
        id = userStatus.getId();
        lastActiveAt = userStatus.getLastActiveAt();

        FindUserStatusResult findUserStatusResult = new FindUserStatusResult( id, userId, lastActiveAt );

        return findUserStatusResult;
    }

    @Override
    public CreateUserStatusCommand toCreateUserStatusCommand(UserStatus userStatus) {
        if ( userStatus == null ) {
            return null;
        }

        UUID userId = null;
        Instant lastActiveAt = null;

        userId = userStatusUserId( userStatus );
        lastActiveAt = userStatus.getLastActiveAt();

        CreateUserStatusCommand createUserStatusCommand = new CreateUserStatusCommand( userId, lastActiveAt );

        return createUserStatusCommand;
    }

    @Override
    public UpdateUserStatusResult toUpdateUserStatusResult(UserStatus userStatus) {
        if ( userStatus == null ) {
            return null;
        }

        UUID userId = null;
        UUID id = null;
        Instant lastActiveAt = null;

        userId = userStatusUserId( userStatus );
        id = userStatus.getId();
        lastActiveAt = userStatus.getLastActiveAt();

        UpdateUserStatusResult updateUserStatusResult = new UpdateUserStatusResult( id, userId, lastActiveAt );

        return updateUserStatusResult;
    }

    private UUID userStatusUserId(UserStatus userStatus) {
        if ( userStatus == null ) {
            return null;
        }
        User user = userStatus.getUser();
        if ( user == null ) {
            return null;
        }
        UUID id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
