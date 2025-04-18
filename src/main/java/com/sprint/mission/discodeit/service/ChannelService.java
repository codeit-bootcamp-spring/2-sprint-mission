package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelResponse;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import java.util.UUID;

public interface ChannelService {

  Channel createPublic(PublicChannelCreateRequest request);

  Channel createPrivate(PrivateChannelCreateRequest request);

  ChannelResponse find(UUID channelId);

  List<ChannelResponse> findAllByUserID(UUID userID);

  Channel update(UUID channelId, PublicChannelUpdateRequest request);

  void delete(UUID channelId);
}
