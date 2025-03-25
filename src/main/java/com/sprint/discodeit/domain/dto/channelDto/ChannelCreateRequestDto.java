package com.sprint.discodeit.domain.dto.channelDto;

import com.sprint.discodeit.domain.ChannelType;
import java.util.List;
import java.util.UUID;

public record ChannelCreateRequestDto(ChannelType type, String name, String description, List<UUID> userId) {
}
