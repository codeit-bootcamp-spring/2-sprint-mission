package com.sprint.mission.discodeit.updater.channel;

import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.UUID;

public interface ChannelUpdater {
    boolean supports(Channel channel, ChannelUpdateRequest channelUpdateRequest);
    void update(UUID channelId, ChannelUpdateRequest request, ChannelRepository repository);
}
