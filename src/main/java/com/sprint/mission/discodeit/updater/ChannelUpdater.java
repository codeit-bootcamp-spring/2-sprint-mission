package com.sprint.mission.discodeit.updater;

import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

public interface ChannelUpdater {
    boolean supports(Channel channel, ChannelUpdateRequest channelUpdateRequest);
    void update(Channel channel, ChannelUpdateRequest request, ChannelRepository repository);
}
