package com.sprint.mission.discodeit.dto.common;

import jakarta.validation.constraints.NotBlank;
import java.time.ZonedDateTime;

public class TimeStamps {

  @NotBlank
  private ZonedDateTime createdAt;
  @NotBlank
  private ZonedDateTime updatedAt;
}
