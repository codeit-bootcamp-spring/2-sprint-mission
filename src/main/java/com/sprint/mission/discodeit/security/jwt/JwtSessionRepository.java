package com.sprint.mission.discodeit.security.jwt;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface JwtSessionRepository extends JpaRepository<JwtSession, UUID> {

  Optional<JwtSession> findByRefreshToken(String refreshToken);

  List<JwtSession> findAllByUserId(UUID userId);

}
