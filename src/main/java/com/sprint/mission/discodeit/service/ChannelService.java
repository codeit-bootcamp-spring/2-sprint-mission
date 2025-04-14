package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.service.dto.channel.ChannelByUserIdResponse;
import com.sprint.mission.discodeit.service.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.service.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.service.dto.channel.PrivateChannelRequest;
import com.sprint.mission.discodeit.service.dto.channel.PublicChannelRequest;
import java.util.List;
import java.util.UUID;

public interface ChannelService {

  ChannelResponse create(PrivateChannelRequest privateRequest);

  ChannelResponse create(PublicChannelRequest publicRequest);

  ChannelByUserIdResponse find(UUID channelId);

  List<ChannelByUserIdResponse> findAllByUserId(UUID userId);

  ChannelResponse update(UUID id, ChannelUpdateRequest updateRequest);

  void delete(UUID channelId);
}