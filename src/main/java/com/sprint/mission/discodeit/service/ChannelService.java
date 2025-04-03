package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelReadResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

public interface ChannelService {
    UUID createPrivateChannel(PrivateChannelCreateRequest privateChannelCreateRequest);
    UUID createPublicChannel(String channelName);
    ChannelReadResponse readChannel(UUID channelId);
    List<ChannelReadResponse> findAllByUserId(UUID userId);
    List<Message> readMessageListByChannelId(UUID channelId);
    void updateChannel(UUID id, ChannelUpdateRequest channelUpdateRequest);
    void addChannelParticipant(UUID channelId, UUID newParticipantId);
    void deleteChannel(UUID channelId);
    static void validateChannelId(UUID channelId, ChannelRepository jcfChannelRepository) {
        if (!jcfChannelRepository.existsById(channelId)) {
            throw new NoSuchElementException("해당 channelId를 가진 채널이 존재하지 않습니다 : " + channelId);
        }
    }
}
