package com.sprint.mission.discodeit.core.channel.usecase;

import com.sprint.mission.discodeit.core.channel.usecase.dto.UpdateChannelCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.UpdateChannelResult;

public interface UpdateChannelUseCase {

  UpdateChannelResult update(UpdateChannelCommand command);

}
