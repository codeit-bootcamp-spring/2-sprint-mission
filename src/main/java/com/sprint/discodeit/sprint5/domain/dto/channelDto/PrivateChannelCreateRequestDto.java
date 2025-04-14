package com.sprint.discodeit.sprint5.domain.dto.channelDto;

import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequestDto(String channelName, String channelDescription, List<UUID> usersIds) {
}
