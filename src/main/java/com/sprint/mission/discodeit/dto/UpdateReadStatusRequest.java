package com.sprint.mission.discodeit.dto;

import java.time.Instant;
import lombok.Data;

@Data
public class UpdateReadStatusRequest {

  private Instant lastActiveAt;
}
