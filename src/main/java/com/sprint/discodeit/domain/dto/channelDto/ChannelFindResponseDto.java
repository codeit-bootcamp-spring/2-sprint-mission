package com.sprint.discodeit.domain.dto.channelDto;

import com.sprint.discodeit.domain.ChannelType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelFindResponseDto(UUID channelId,
                                     String channelName,
                                     Instant lastMessageTime,
                                     ChannelType channelType,
                                     List<UUID> userIds // PRIVATE 채널일 경우 참여한 User ID 목록
                                      ) {
}
