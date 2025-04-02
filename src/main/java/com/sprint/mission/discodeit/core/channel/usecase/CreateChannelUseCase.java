package com.sprint.mission.discodeit.core.channel.usecase;

import com.sprint.mission.discodeit.adapter.inbound.channel.dto.PrivateChannelCreateCommand;
import com.sprint.mission.discodeit.adapter.inbound.channel.dto.PublicChannelCreateCommand;
import java.util.UUID;

public interface CreateChannelUseCase {

  UUID create(PublicChannelCreateCommand command);

  UUID create(PrivateChannelCreateCommand command);

}
