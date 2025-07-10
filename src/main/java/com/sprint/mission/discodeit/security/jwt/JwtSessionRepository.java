package com.sprint.mission.discodeit.security.jwt;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtSessionRepository extends JpaRepository<JwtSession, UUID> {

    Optional<JwtSession> findByRefreshToken(String refreshToken);

    Optional<JwtSession> findByUserId(UUID userId);

}
