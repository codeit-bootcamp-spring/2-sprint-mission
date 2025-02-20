package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {
    Channel createChannel(String name);
    Optional<Channel> getChannelById(UUID id);
    Optional<Channel> getChannelByName(String name);
    List<Channel> getAllChannels();
    void updateChannelName(UUID id, String name);
    void deleteChannel(UUID id);
}