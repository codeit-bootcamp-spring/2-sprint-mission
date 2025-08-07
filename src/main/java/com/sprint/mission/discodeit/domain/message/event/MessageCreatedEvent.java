package com.sprint.mission.discodeit.domain.message.event;

import com.sprint.mission.discodeit.domain.channel.entity.ChannelType;
import com.sprint.mission.discodeit.domain.message.entity.Message;
import java.util.UUID;

public record MessageCreatedEvent(
    UUID userId,
    UUID channelId,
    String messageContent,
    String channelName,
    ChannelType channelType
) {

  public static MessageCreatedEvent from(Message message) {
    return new MessageCreatedEvent(
        message.getUser().getId(),
        message.getChannel().getId(),
        message.getContext(),
        message.getChannel().getName(),
        message.getChannel().getType()
    );
  }

}
