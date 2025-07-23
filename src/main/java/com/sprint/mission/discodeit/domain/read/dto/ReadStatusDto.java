package com.sprint.mission.discodeit.domain.read.dto;

import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.read.entity.ReadStatus;
import com.sprint.mission.discodeit.domain.user.entity.User;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ReadStatusDto(
    UUID id,
    UUID userId,
    UUID channelId,
    Instant lastReadAt,
    boolean notificationEnabled
) {

  public static ReadStatusDto from(ReadStatus readStatus) {
    User user = readStatus.getUser();
    Channel channel = readStatus.getChannel();
    return ReadStatusDto.builder()
        .id(readStatus.getId())
        .userId(user != null ? user.getId() : null)
        .channelId(channel != null ? channel.getId() : null)
        .lastReadAt(readStatus.getLastReadAt())
        .notificationEnabled(readStatus.isNotificationEnabled())
        .build();
  }

}
