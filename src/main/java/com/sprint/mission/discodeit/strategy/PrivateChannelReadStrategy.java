package com.sprint.mission.discodeit.strategy;

import com.sprint.mission.discodeit.dto.channel.ChannelReadResponse;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelReadResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.model.ChannelType;
import org.springframework.stereotype.Component;

@Component
public class PrivateChannelReadStrategy implements ChannelReadStrategy {
    @Override
    public ChannelType getSupportChannelType() {
        return ChannelType.PRIVATE;
    }

    @Override
    public ChannelReadResponse toDto(Channel channel) {
        return new PrivateChannelReadResponse(channel.getId(), channel.getLastMessageTime(), channel.getParticipantIds());
    }
}
