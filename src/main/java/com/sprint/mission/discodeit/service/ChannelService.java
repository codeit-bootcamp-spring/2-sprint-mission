package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

  ChannelDto createPublicChannel(PublicChannelCreateRequest request);

  ChannelDto createPrivateChannel(PrivateChannelCreateRequest request);

  ChannelDto find(UUID channelId);

  List<ChannelDto> findAllByUserId(UUID userId);

  ChannelDto update(UUID channelId, PublicChannelUpdateRequest request);

  void delete(UUID channelId);
}
