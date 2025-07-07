package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("channelSecurity")
@RequiredArgsConstructor
public class ChannelSecurity {

  private final ChannelRepository channelRepository;

  public boolean isPublic(UUID channelId) {
    return channelRepository.findById(channelId)
        .map(Channel::getType)
        .map(type -> type == ChannelType.PUBLIC)
        .orElse(false);
  }
}
