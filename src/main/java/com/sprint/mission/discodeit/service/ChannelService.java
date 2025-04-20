package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.ChannelDto;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

  ChannelDto createPrivateChannel(PrivateChannelCreateRequest request);

  ChannelDto createPublicChannel(PublicChannelCreateRequest request);

  ChannelDto findChannelById(UUID channelId);

  List<ChannelDto> findAllByUserId(UUID userId);

  ChannelDto updateChannel(UUID channelId, PublicChannelUpdateRequest request);

  void deleteChannel(UUID channelId);

}
