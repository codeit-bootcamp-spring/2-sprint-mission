package com.sprint.mission.discodeit.core.channel.service;


import com.sprint.mission.discodeit.core.channel.dto.ChannelDto;
import com.sprint.mission.discodeit.core.channel.dto.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.core.channel.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.core.channel.dto.request.PublicChannelCreateRequest;
import java.util.UUID;

public interface ChannelService {

  ChannelDto create(PublicChannelCreateRequest request);

  ChannelDto create(PrivateChannelCreateRequest request);

  ChannelDto update(UUID channelId, ChannelUpdateRequest request);

  void delete(UUID channelId);

}
