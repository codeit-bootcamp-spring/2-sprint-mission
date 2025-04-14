package com.sprint.discodeit.sprint5.domain.dto.channelDto;

import com.sprint.discodeit.sprint5.domain.ChannelType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelFindResponseDto(UUID channelId,
                                     String channelName,
                                     Instant lastMessageTime,
                                     ChannelType channelType,
                                     List<UUID> usersIds // PRIVATE 채널일 경우 참여한 users ID 목록
                                      ) {
}
