package com.sprint.mission.discodeit.dto.user;

import java.time.Instant;
import java.util.UUID;
import lombok.Data;

@Data
public class UserInfoDto {

  private UUID id;
  private Instant createAt;
  private Instant updateAt;
  private String username;
  private String email;
  private UUID profileId;
  private Boolean online;
}
