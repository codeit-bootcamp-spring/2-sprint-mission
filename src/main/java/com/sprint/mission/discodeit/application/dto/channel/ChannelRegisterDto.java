package com.sprint.mission.discodeit.application.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.UUID;

public record ChannelRegisterDto(ChannelType channelType, String name, UUID logInUserId) {
}
