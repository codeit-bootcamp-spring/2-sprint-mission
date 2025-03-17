package com.sprint.mission.discodeit.provider;

import com.sprint.mission.discodeit.model.ChannelType;
import com.sprint.mission.discodeit.strategy.ChannelReadStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DefaultChannelReadStrategyProvider implements ChannelReadStrategyProvider {
    private final Map<ChannelType, ChannelReadStrategy> strategyMap;

    public DefaultChannelReadStrategyProvider(List<ChannelReadStrategy> channelReadStrategies) {
        this.strategyMap = channelReadStrategies.stream().collect(Collectors.toMap(ChannelReadStrategy::getSupportChannelType, Function.identity()));
    }

    @Override
    public ChannelReadStrategy getChannelReadStrategy(ChannelType channelType) {
        ChannelReadStrategy strategy = strategyMap.get(channelType);
        if (strategy == null) {
            throw new IllegalArgumentException("지원하지 않는 채널 타입입니다 : " + channelType);
        }
        return strategy;
    }
}
