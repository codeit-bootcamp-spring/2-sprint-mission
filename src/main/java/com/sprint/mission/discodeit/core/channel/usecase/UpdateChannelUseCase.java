package com.sprint.mission.discodeit.core.channel.usecase;

import com.sprint.mission.discodeit.core.channel.usecase.dto.ChannelResult;
import com.sprint.mission.discodeit.core.channel.usecase.dto.UpdateChannelCommand;

public interface UpdateChannelUseCase {

  ChannelResult update(UpdateChannelCommand command);

}
