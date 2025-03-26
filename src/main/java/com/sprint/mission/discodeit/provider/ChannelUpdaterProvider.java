package com.sprint.mission.discodeit.provider;

import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.updater.ChannelUpdater;

import java.util.List;

public interface ChannelUpdaterProvider {
    List<ChannelUpdater> getApplicableUpdaters(Channel channel, ChannelUpdateRequest request);
}
