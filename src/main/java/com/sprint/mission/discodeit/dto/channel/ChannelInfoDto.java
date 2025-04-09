package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class ChannelInfoDto {

  private UUID id;
  private ChannelType type;
  private String name;
  private String description;
  private Instant lastMessageAt;
  private List<UUID> participantIds;
}
