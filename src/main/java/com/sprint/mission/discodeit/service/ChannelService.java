package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.controller.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.controller.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.controller.dto.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

  Channel create(PublicChannelCreateRequest request);

  Channel create(PrivateChannelCreateRequest request);

  ChannelDto find(UUID getChannelId);

  List<ChannelDto> findAllByUserId(UUID userId);

  Channel update(UUID getChannelId, PublicChannelUpdateRequest request);

  void delete(UUID getChannelId);
}