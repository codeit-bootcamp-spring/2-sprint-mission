package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.constant.ChannelType;

import java.util.List;
import java.util.UUID;

public record SaveChannelParamDto(
    String channelName,
    ChannelType channelType,
    List<UUID> userList
) {
    public static SaveChannelParamDto createPublic(String channelName) {
        return new SaveChannelParamDto(channelName, ChannelType.PUBLIC, List.of());
    }

    public static SaveChannelParamDto createPrivate(String channelName, List<UUID> userList) {
        return new SaveChannelParamDto(channelName, ChannelType.PRIVATE, userList);
    }

    private SaveChannelParamDto getSaveChannelParamDto() {
        return this;
    }
}
