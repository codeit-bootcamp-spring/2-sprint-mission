package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.SaveChannelResponseDto;
import com.sprint.mission.discodeit.dto.PublicChannelUpdateRequest;
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
