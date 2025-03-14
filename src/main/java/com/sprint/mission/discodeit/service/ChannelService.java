package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

public interface ChannelService {
    Channel createPrivateChannel(PrivateChannelCreateRequest privateChannelCreateRequest);
    Channel createPublicChannel(String channelName);
    Channel readChannel(UUID channelId);
    Map<UUID, Channel> readAllChannels();
    List<Message> readMessageListByChannelId(UUID channelId);
    void updateChannelName(UUID channelId, String newChannelName);
    void addChannelParticipant(UUID channelId, UUID newParticipantId);
    void deleteChannel(UUID channelId);
    static void validateChannelId(UUID channelId, ChannelRepository jcfChannelRepository) {
        if (!jcfChannelRepository.existsById(channelId)) {
            throw new NoSuchElementException("해당 channelId를 가진 채널이 존재하지 않습니다 : " + channelId);
        }
    }
}
