package com.sprint.mission.discodeit.dto.readStatus;

import java.time.Instant;
import lombok.Data;

@Data
public class UpdateReadStatusRequest {

  private Instant newLastReadAt;
}
