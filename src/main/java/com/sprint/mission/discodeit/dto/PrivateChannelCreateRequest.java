package com.sprint.mission.discodeit.dto;

import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequest(
    String channelName,
    List<UUID> userList
) {

}
