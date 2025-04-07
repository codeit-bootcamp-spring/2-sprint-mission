package com.sprint.mission.discodeit.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(name = "User")
@Getter
@AllArgsConstructor
public class AuthResponseDto {

  private UUID id;
  private Instant createdAt;
  private Instant updatedAt;
  //
  private String username;
  private String password;
  private String email;
  //
  private UUID profileId;
}
