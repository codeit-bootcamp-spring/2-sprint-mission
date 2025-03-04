package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

public interface ChannelService {
    void createChannel(String channelName);
    Channel readChannel(UUID channelId);
    Map<UUID, Channel> readAllChannels();
    List<Message> readMessageListByChannelId(UUID channelId);
    void updateChannelName(UUID channelId, String newChannelName);
    void addChannelParticipant(UUID channelId, User newParticipant);
    void deleteChannel(UUID channelId);
    static void validateChannelId(UUID channelId, ChannelRepository channelRepository) {
        if (!channelRepository.existsChannel(channelId)) {
            throw new NoSuchElementException("해당 channelId를 가진 채널이 존재하지 않습니다 : " + channelId);
        }
    }
}
