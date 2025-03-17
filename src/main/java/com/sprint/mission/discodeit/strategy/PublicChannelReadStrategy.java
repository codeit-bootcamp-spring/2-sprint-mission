package com.sprint.mission.discodeit.strategy;

import com.sprint.mission.discodeit.dto.channel.ChannelReadResponse;
import com.sprint.mission.discodeit.dto.channel.PublicChannelReadResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.model.ChannelType;
import org.springframework.stereotype.Component;

@Component
public class PublicChannelReadStrategy implements ChannelReadStrategy {
    @Override
    public ChannelType getSupportChannelType() {
        return ChannelType.PUBLIC;
    }

    @Override
    public ChannelReadResponse toDto(Channel channel) {
        return new PublicChannelReadResponse(channel.getId(), channel.getLastMessageTime(), channel.getChannelName());
    }
}