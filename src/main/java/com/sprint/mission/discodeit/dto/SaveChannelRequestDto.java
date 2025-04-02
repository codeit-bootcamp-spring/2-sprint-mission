package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.constant.ChannelType;

import java.util.List;
import java.util.UUID;

public record SaveChannelRequestDto(
    String channelName,
    List<UUID> userList
) {

}
