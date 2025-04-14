package com.sprint.discodeit.sprint.domain.dto.channelDto;

import com.sprint.discodeit.sprint.domain.ChannelType;
import java.time.Instant;

public record ChannelResponseDto(Long channelId,
                                 String channelName,
                                 Instant lastMessageTime,
                                 ChannelType channelType
                                      ){
}
