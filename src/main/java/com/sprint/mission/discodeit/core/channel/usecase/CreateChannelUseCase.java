package com.sprint.mission.discodeit.core.channel.usecase;

import com.sprint.mission.discodeit.core.channel.usecase.dto.ChannelDto;
import com.sprint.mission.discodeit.core.channel.usecase.dto.CreatePrivateChannelCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.CreatePublicChannelCommand;

public interface CreateChannelUseCase {

  ChannelDto create(CreatePublicChannelCommand command);

  ChannelDto create(CreatePrivateChannelCommand command);

}
