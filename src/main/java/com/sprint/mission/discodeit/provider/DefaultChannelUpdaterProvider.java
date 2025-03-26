package com.sprint.mission.discodeit.provider;

import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.updater.ChannelUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DefaultChannelUpdaterProvider implements ChannelUpdaterProvider {
    private final List<ChannelUpdater> updaters;

    @Override
    public List<ChannelUpdater> getApplicableUpdaters(Channel channel, ChannelUpdateRequest request) {
        return updaters.stream()
                .filter(updater -> updater.supports(channel, request))
                .toList();
    }
}
