package com.sprint.mission.discodeit.dto.user;

import lombok.Data;

@Data
public class UpdateUserRequest {

  private String newUsername;
  private String newEmail;
  private String newPassword;
}
