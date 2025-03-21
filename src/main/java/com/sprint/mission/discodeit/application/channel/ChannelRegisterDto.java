package com.sprint.mission.discodeit.application.channel;

import com.sprint.mission.discodeit.application.user.UserDto;
import com.sprint.mission.discodeit.entity.ChannelType;

public record ChannelRegisterDto(ChannelType channelType, String name, UserDto owner) {
}
