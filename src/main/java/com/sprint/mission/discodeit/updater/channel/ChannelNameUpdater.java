package com.sprint.mission.discodeit.updater.channel;

import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Component;

@Component
public class ChannelNameUpdater implements ChannelUpdater {
    @Override
    public boolean supports(Channel channel, ChannelUpdateRequest channelUpdateRequest) {
        return !channel.getChannelName().equals(channelUpdateRequest.channelName());
    }

    @Override
    public void update(Channel channel, ChannelUpdateRequest request, ChannelRepository channelRepository) {
        channelRepository.updateChannelName(channel.getId(), request.channelName());
    }
}
