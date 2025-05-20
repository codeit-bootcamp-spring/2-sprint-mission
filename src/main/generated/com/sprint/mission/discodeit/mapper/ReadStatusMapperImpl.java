package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import java.time.Instant;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-08T18:36:00+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.11 (Oracle Corporation)"
)
@Component
public class ReadStatusMapperImpl implements ReadStatusMapper {

    @Override
    public ReadStatusDto toDto(ReadStatus readStatus) {
        if ( readStatus == null ) {
            return null;
        }

        UUID id = null;
        Instant lastReadAt = null;

        id = readStatus.getId();
        lastReadAt = readStatus.getLastReadAt();

        UUID userId = null;
        UUID channelId = null;

        ReadStatusDto readStatusDto = new ReadStatusDto( id, userId, channelId, lastReadAt );

        return readStatusDto;
    }
}
