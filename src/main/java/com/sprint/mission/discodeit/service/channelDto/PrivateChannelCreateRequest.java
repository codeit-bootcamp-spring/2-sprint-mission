package com.sprint.mission.discodeit.service.channelDto;


import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequest (
    UUID ownerId,
    List<UUID> userIds
){}