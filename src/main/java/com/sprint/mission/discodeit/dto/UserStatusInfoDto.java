package com.sprint.mission.discodeit.dto;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStatusInfoDto {

  private UUID id;
  private UUID userid;
  private boolean status;
  private Instant updatedAt;
}
