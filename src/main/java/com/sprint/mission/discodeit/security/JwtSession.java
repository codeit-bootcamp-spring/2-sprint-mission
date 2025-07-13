package com.sprint.mission.discodeit.security;

import lombok.*;

import java.time.Instant;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtSession {
  private UUID id;
  private com.sprint.mission.discodeit.dto.data.UserDto user;
  private String accessToken;
  private String refreshToken;
  private Instant issuedAt;
  private Instant expiresAt;
}

