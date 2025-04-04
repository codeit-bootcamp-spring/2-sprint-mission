package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.controller.user.UpdateUserStatusResponseDTO;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-03T21:07:51+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Oracle Corporation)"
)
@Component
public class UserStatusMapperImpl implements UserStatusMapper {

    @Override
    public UpdateUserStatusResponseDTO toUpdateUserStatusResponseDTO(UserStatus userStatus) {
        if ( userStatus == null ) {
            return null;
        }

        UUID id = null;
        UUID userId = null;
        Instant lastActiveAt = null;

        id = userStatus.getId();
        userId = userStatus.getUserId();
        lastActiveAt = userStatus.getLastActiveAt();

        UpdateUserStatusResponseDTO updateUserStatusResponseDTO = new UpdateUserStatusResponseDTO( id, userId, lastActiveAt );

        return updateUserStatusResponseDTO;
    }
}
