package com.sprint.mission.discodeit.application.channeldto;

import com.sprint.mission.discodeit.application.userdto.UserDto;
import com.sprint.mission.discodeit.entity.ChannelType;

public record ChannelRegisterDto(ChannelType channelType, String name, UserDto owner) {
}
