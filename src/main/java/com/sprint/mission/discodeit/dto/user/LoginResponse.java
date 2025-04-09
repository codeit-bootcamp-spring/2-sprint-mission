package com.sprint.mission.discodeit.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

  private boolean success;
  private String message;
  private String token;
}
