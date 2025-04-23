package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.service.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.service.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.service.channel.PrivateChannelRequest;
import com.sprint.mission.discodeit.dto.service.channel.PublicChannelRequest;
import java.util.List;
import java.util.UUID;

public interface ChannelService {

  ChannelDto create(PrivateChannelRequest privateRequest);

  ChannelDto create(PublicChannelRequest publicRequest);

  ChannelDto find(UUID channelId);

  List<ChannelDto> findAllByUserId(UUID userId);

  ChannelDto update(UUID id, ChannelUpdateRequest updateRequest);

  void delete(UUID channelId);
}