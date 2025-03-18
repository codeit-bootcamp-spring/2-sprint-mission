package com.sprint.discodeit.domain.dto.channelDto;

import java.util.UUID;

public record ChannelUpdateRequestDto(UUID channelId, String newName, String newDescription) {
}
