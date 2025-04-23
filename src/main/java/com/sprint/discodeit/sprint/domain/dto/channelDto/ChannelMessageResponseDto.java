package com.sprint.discodeit.sprint.domain.dto.channelDto;

import java.time.Instant;

public record ChannelMessageResponseDto( Long messageId,
                                         String content,
                                         String senderName,
                                         Instant createdAt) {
}
