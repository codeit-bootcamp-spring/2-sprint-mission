package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import java.util.UUID;

public interface ChannelService {

  ChannelDto createPublicChannel(PublicChannelCreateRequest publicChannelCreateRequest);

  ChannelDto createPrivateChannel(
      PrivateChannelCreateRequest privateChannelCreateRequest);

  List<ChannelDto> findAllByUserId(UUID userId);

  ChannelDto updateChannel(UUID channelId, PublicChannelUpdateRequest channelUpdateParamDto);

  void deleteChannel(UUID channelUUID);
}
