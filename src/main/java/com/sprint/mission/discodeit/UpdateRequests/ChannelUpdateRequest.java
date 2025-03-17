package com.sprint.mission.discodeit.UpdateRequests;

import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.Getter;

@Getter
public class ChannelUpdateRequest {
    private ChannelType channelType;
    private String channelName;
}
