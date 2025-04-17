package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.service.channel.*;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

  CreatePublicChannelResult createPublicChannel(
      CreatePublicChannelCommand createPublicChannelCommand);

  CreatePrivateChannelResult createPrivateChannel(
      CreatePrivateChannelCommand createPrivateChannelCommand);

  FindChannelResult find(UUID id);

  List<FindChannelResult> findAllByUserId(UUID userId);

  UpdateChannelResult update(UUID id, UpdateChannelCommand updateChannelCommand);

  void delete(UUID id);
}
