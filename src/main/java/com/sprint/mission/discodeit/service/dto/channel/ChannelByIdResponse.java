package com.sprint.mission.discodeit.service.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ChannelByIdResponse {
    private final Instant lastMessageTime;
    private final Channel channel;
    private List<UUID> idList;   // PRIVATE일 경우에만.
}
