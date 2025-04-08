package com.sprint.mission.discodeit.core.channel.usecase;

import com.sprint.mission.discodeit.core.channel.usecase.dto.CreatePrivateChannelCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.CreatePrivateChannelResult;
import com.sprint.mission.discodeit.core.channel.usecase.dto.CreatePublicChannelCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.CreatePublicChannelResult;

public interface CreateChannelUseCase {

  CreatePublicChannelResult create(CreatePublicChannelCommand command);

  CreatePrivateChannelResult create(CreatePrivateChannelCommand command);

}
