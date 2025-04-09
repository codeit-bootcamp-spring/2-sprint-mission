package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.dto.ChannelResponseDto;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

  Channel createPrivateChannel(PrivateChannelCreateRequest request);

  Channel createPublicChannel(PublicChannelCreateRequest request);

  ChannelResponseDto findChannelById(UUID channelId);

  List<ChannelResponseDto> findAllByUserId(UUID userId);

  Channel updateChannel(UUID channelId, PublicChannelUpdateRequest request);

  void deleteChannel(UUID channelId);

}
