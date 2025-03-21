package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.entity.ChannelType;

public record ChannelRegisterDto(ChannelType channelType, String name, UserDto owner) {
}
