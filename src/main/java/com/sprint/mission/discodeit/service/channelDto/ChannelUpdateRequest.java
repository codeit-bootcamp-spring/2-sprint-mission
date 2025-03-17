package com.sprint.mission.discodeit.service.channelDto;

import java.util.UUID;

public record ChannelUpdateRequest (
    UUID channelId,
    String name,
    String description
){}