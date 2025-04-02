package com.sprint.mission.discodeit.core.channel.usecase;

import com.sprint.mission.discodeit.core.channel.usecase.dto.CreatePrivateChannelCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.CreatePublicChannelCommand;
import java.util.UUID;

public interface CreateChannelUseCase {

  UUID create(CreatePublicChannelCommand command);

  UUID create(CreatePrivateChannelCommand command);

}
