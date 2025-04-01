package com.sprint.mission.discodeit.dto.channel;

import java.util.UUID;
import lombok.Getter;

@Getter
public class ChannelUpdateRequestDto {
    private UUID channelId;
    private String newChannelName;
    private String newChannelDescription;
}
