package com.sprint.mission.discodeit.core.channel.usecase;

import com.sprint.mission.discodeit.core.channel.usecase.dto.ChannelDto;
import com.sprint.mission.discodeit.core.channel.usecase.dto.UpdateChannelCommand;

public interface UpdateChannelUseCase {

  ChannelDto update(UpdateChannelCommand command);

}
