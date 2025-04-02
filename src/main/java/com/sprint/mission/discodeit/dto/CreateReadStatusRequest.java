package com.sprint.mission.discodeit.dto;

import java.time.Instant;
import java.util.UUID;
import lombok.Data;

@Data
public class CreateReadStatusRequest {

  private UUID userId;
  private UUID channelId;
  private Instant lastReadAt;
}
