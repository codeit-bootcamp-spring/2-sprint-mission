package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.SaveChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface ChannelService {

  SaveChannelResponseDto createPublicChannel(PublicChannelCreateRequest publicChannelCreateRequest);

  SaveChannelResponseDto createPrivateChannel(
      PrivateChannelCreateRequest privateChannelCreateRequest);

  ChannelDto findChannel(UUID channelId);

  List<ChannelDto> findAllByUserId(UUID userId);

  void updateChannel(UUID channelId, PublicChannelUpdateRequest channelUpdateParamDto);

  void deleteChannel(UUID channelUUID);
}
