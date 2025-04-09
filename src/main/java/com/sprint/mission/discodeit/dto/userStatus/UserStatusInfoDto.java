package com.sprint.mission.discodeit.dto.userStatus;

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
