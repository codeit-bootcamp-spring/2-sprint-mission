package com.sprint.mission.discodeit.dto.userStatus;

import java.time.Instant;
import lombok.Data;

@Data
public class UserStatusUpdateRequest {

  private Instant newLastActiveAt;
}
