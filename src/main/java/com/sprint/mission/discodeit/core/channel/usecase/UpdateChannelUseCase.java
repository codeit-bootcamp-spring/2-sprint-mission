package com.sprint.mission.discodeit.core.channel.usecase;

import com.sprint.mission.discodeit.core.channel.usecase.dto.ChannelDto;
import com.sprint.mission.discodeit.core.channel.usecase.dto.ChannelUpdateCommand;

public interface UpdateChannelUseCase {

  ChannelDto update(ChannelUpdateCommand command);

}
