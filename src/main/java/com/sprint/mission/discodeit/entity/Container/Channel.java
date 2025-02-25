package com.sprint.mission.discodeit.entity.Container;

import com.sprint.mission.discodeit.Repository.ChannelRepository;

public class Channel extends Container {
    private final ChannelRepository channelRepository;

    public Channel(String name) {
        super(name);
        channelRepository = new ChannelRepository();
    }

    public ChannelRepository getChannelRepository() {
        return channelRepository;
    }
}
