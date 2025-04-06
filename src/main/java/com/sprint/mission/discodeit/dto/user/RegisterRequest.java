package com.sprint.mission.discodeit.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest {

  private String username;
  private String email;
  private String password;
}
