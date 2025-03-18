package com.sprint.discodeit.domain.dto.channelDto;

import com.sprint.discodeit.domain.ChannelType;
import com.sprint.discodeit.domain.entity.ReadStatus;

public record ChannelResponseDto(String channelName, ChannelType type, ReadStatus readStatus) {
}
