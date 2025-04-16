package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.service.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.service.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.service.dto.channel.PrivateChannelRequest;
import com.sprint.mission.discodeit.service.dto.channel.PublicChannelRequest;
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