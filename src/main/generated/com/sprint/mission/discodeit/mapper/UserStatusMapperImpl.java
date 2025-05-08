package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-07T17:09:02+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.11 (Oracle Corporation)"
)
@Component
public class UserStatusMapperImpl implements UserStatusMapper {

    @Override
    public UserStatusDto toDto(UserStatus userStatus) {
        if ( userStatus == null ) {
            return null;
        }

        UUID id = null;
        Instant lastActiveAt = null;

        id = userStatus.getId();
        lastActiveAt = userStatus.getLastActiveAt();

        UUID userid = null;

        UserStatusDto userStatusDto = new UserStatusDto( id, userid, lastActiveAt );

        return userStatusDto;
    }
}
