package com.sprint.mission.discodeit.service.channelDto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class ChannelResponse {
    private UUID channelId;
    private String type;
    private Instant latestMessageTime;
    private List<UUID> userIds;

    public ChannelResponse(Channel channel, Message latestMessage, List<UUID> userIds) {
        this.channelId = channel.getId();
        this.type = channel.getType().name();

        this.latestMessageTime = latestMessage != null ? latestMessage.getUpdatedAt() : null;
        this.userIds = userIds;
    }
}
