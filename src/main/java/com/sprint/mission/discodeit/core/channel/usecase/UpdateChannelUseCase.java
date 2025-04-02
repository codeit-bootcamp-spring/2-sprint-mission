package com.sprint.mission.discodeit.core.channel.usecase;

import com.sprint.mission.discodeit.adapter.inbound.channel.dto.UpdateChannelCommand;

public interface UpdateChannelUseCase {

  void update(UpdateChannelCommand command);

}
