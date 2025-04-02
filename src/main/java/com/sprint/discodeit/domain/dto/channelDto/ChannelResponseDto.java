package com.sprint.discodeit.domain.dto.channelDto;

import com.sprint.discodeit.domain.ChannelType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelResponseDto(UUID channelId,
                                 String channelName,
                                 Instant lastMessageTime,
                                 ChannelType channelType
                                      ){
}
