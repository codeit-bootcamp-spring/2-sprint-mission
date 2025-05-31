package com.sprint.mission.discodeit.core.channel.usecase;

import com.sprint.mission.discodeit.core.channel.usecase.dto.ChannelDto;
import java.util.List;
import java.util.UUID;

public interface FindChannelUseCase {

  List<ChannelDto> findAllByUserId(UUID userId);

}
