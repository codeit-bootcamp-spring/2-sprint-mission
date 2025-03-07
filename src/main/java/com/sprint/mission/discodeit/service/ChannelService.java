package com.sprint.mission.discodeit.service;

import java.util.UUID;

public interface ChannelService {
    UUID createChannel();

    void searchChannel(UUID id);

    void searchAllChannels();

    void updateChannel(UUID id);

    void deleteChannel(UUID id);
}
