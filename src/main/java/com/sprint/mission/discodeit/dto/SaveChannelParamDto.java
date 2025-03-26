package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.constant.ChannelType;

import java.util.List;
import java.util.UUID;

public record SaveChannelParamDto(
    String channelName,
    ChannelType channelType,
    List<UUID> userList
) {
    public SaveChannelParamDto {
        if (channelType.equals(ChannelType.PUBLIC) && userList != null && !userList.isEmpty()) {
            throw new IllegalArgumentException("PUBLIC 채널에서는 사용자 리스트를 설정할 수 없음");
        }
    }
}
