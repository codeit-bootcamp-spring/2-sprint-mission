package com.sprint.mission.discodeit.dto.readstatus;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReadStatusDto {

    private UUID id;
    private UUID userId;
    private UUID channelId;
    private Instant lastReadAt;
}
