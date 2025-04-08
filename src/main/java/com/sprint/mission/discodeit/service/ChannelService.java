package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.ChannelDto.Response;
import com.sprint.mission.discodeit.dto.common.ListSummary;
import java.util.List;
import java.util.UUID;

public interface ChannelService {

  ChannelDto.Response createPrivateChannel(ChannelDto.CreatePrivate dto);

  ChannelDto.Response createPublicChannel(ChannelDto.CreatePublic dto);

  ChannelDto.Response getChannelDetails(UUID channelId);

  List<ChannelDto.Response> findAllByUserId(UUID userId);
  
  ListSummary<Response> findPublicChannels();

  ChannelDto.Response updateChannel(ChannelDto.Update dto, UUID ownerId);

  void deleteChannel(UUID channelId, UUID ownerId);

  List<ChannelDto.Response> getAccessibleChannels(UUID userId);
}

