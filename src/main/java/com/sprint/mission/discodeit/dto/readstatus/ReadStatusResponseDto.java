package com.sprint.mission.discodeit.dto.readstatus;

import com.sprint.mission.discodeit.entity.status.ReadStatus;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReadStatusResponseDto {

    private UUID id;
    private UUID channelId;
    private UUID userId;
    private Instant updatedAt;
    private Instant lastReadAt;
    
    public static ReadStatusResponseDto from(ReadStatus readStatus) {
        return new ReadStatusResponseDto(
            readStatus.getId(),
            readStatus.getChannelId(),
            readStatus.getUserId(),
            readStatus.getUpdatedAt(),
            readStatus.getLastReadAt()
        );
    }
}
