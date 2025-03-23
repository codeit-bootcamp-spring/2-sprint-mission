package com.sprint.mission.discodeit.application.dto.channel;

import com.sprint.mission.discodeit.application.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.ChannelType;

public record ChannelRegisterDto(ChannelType channelType, String name, UserDto owner) {
}
