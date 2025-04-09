package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelRequest;
import java.util.List;
import java.util.UUID;

public interface ChannelService {

    UUID createPublicChannel(CreatePublicChannelRequest request);

    UUID createPrivateChannel(CreatePrivateChannelRequest request);

    ChannelResponseDto findByChannelId(UUID channelId);


    List<ChannelResponseDto> findAll();

    List<ChannelResponseDto> findAllByUserId(UUID userId);

    UUID update(UUID channelId, UpdateChannelRequest request);

    void remove(UUID channelId);

    void addMember(UUID channelId, UUID userId);

    void removeMember(UUID channelId, UUID userId);
}
