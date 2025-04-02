package com.sprint.discodeit.sprint5.domain.dto.channelDto;

import com.sprint.discodeit.sprint5.domain.ChannelType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelSummaryResponseDto(UUID  channelId,
                                        String channelName,
                                        String channelDescription,
                                        ChannelType channelType,
                                        Instant latestMessageAt,
                                        List<UUID> participantUserIds) {
}
