package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.dto.data.UserDto;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.GrantedAuthority;

public interface JwtService {

  JwtSession generateTokens(UserDto userDto);

  public boolean isValidToken(String token);

  Optional<JwtSession> reissueAccessToken(String refreshToken);

  void revokeRefreshToken(String refreshToken);

  public String extractUsername(String token);

  public List<GrantedAuthority> extractAuthorities(String token);
}

