package com.sprint.discodeit.sprint.domain.dto.channelDto;

import com.sprint.discodeit.sprint.domain.ChannelType;
import java.time.Instant;
import java.util.List;

public record ChannelFindResponseDto(Long channelId,
                                     String channelName,
                                     Instant lastMessageTime,
                                     ChannelType channelType,
                                     List<String> usersName
                                      ) {
}
