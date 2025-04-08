package com.sprint.mission.discodeit.dto.user;

import java.util.UUID;
import lombok.Getter;

@Getter
public class UserUpdateRequestDto {

  private UUID userID;
  private String newUserName;
  private String newEmail;
  private String newPassword;
}
