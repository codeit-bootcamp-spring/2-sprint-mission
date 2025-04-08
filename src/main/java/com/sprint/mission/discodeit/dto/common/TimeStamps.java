package com.sprint.mission.discodeit.dto.common;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class TimeStamps {

  @NotNull
  private ZonedDateTime createdAt;
  @NotNull
  private ZonedDateTime updatedAt;
}
