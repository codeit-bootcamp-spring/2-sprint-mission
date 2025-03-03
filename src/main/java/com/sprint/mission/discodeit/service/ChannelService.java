package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.ArrayDeque;
import java.util.List;
import java.util.UUID;

public interface ChannelService {
    void createChannel(String channelName);
    Channel readChannel(UUID channelId);
    ArrayDeque<Channel> readAllChannels();
    List<Message> readMessageListByChannelId(UUID channelId);
    void updateChannelName(UUID channelId, String newChannelName);
    void addChannelParticipant(UUID channelId, User newParticipant);
    void deleteChannel(UUID channelId);
    static void validateChannelId(UUID channelId, ChannelRepository channelRepository) {
        if (channelId == null) {
            throw new IllegalArgumentException("input channelId is null!!!");
        }
        channelRepository.findChannelById(channelId);
    }
}
