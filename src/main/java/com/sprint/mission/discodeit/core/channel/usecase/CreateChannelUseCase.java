package com.sprint.mission.discodeit.core.channel.usecase;

import com.sprint.mission.discodeit.core.channel.usecase.dto.ChannelDto;
import com.sprint.mission.discodeit.core.channel.usecase.dto.PrivateChannelCreateCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.PublicChannelCreateCommand;

public interface CreateChannelUseCase {

  ChannelDto create(PublicChannelCreateCommand command);

  ChannelDto create(PrivateChannelCreateCommand command);

}
