package com.sprint.discodeit.sprint.domain.dto.channelDto;

import com.sprint.discodeit.sprint.domain.ChannelType;
import java.time.Instant;
import java.util.UUID;

public record ChannelResponseDto(UUID channelId,
                                 String channelName,
                                 Instant lastMessageTime,
                                 ChannelType channelType
                                      ){
}
