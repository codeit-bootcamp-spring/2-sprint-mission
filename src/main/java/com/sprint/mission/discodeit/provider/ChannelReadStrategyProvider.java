package com.sprint.mission.discodeit.provider;

import com.sprint.mission.discodeit.model.ChannelType;
import com.sprint.mission.discodeit.strategy.ChannelReadStrategy;

public interface ChannelReadStrategyProvider {
    ChannelReadStrategy getChannelReadStrategy(ChannelType channelType);
}
