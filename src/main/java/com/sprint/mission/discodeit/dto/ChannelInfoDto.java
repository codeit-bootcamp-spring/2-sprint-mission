package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.ChannelType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class ChannelInfoDto {

  private UUID channelId;
  private ChannelType channelType;
  private String channelName;
  private String description;
  private Instant lastMessageTime;
  private List<UUID> participantsUserIds;
}
