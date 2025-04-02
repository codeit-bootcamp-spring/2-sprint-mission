package com.sprint.mission.discodeit.dto;

import java.util.UUID;
import lombok.Data;

@Data
public class CreateMessageRequest {

  private String content;
  private UUID channelId;
  private UUID authorId;
}
