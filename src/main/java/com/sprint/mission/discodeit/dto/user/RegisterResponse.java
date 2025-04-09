package com.sprint.mission.discodeit.dto.user;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {

  private UUID userId;
  private boolean success;
  private String message;
}
