package com.sprint.discodeit.domain.entity;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReadStatus {

    private UUID id;
    private UUID userId;
    private UUID channelId;
    private Instant lastReadMessageTime;

}
