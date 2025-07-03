package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.CsrfToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CsrfTokenJpaRepository extends JpaRepository<CsrfToken, Long> {

    Optional<CsrfToken> findBySessionId(String sessionId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CsrfToken c WHERE c.sessionId = :sessionId")
    void deleteBySessionId(String sessionId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CsrfToken c WHERE c.expiresAt < :expirationTime")
    int deleteByExpiresAtBefore(LocalDateTime expirationTime);

    // 세션 ID로 토큰 업데이트 (있으면 업데이트, 없으면 무시)
    @Modifying
    @Transactional
    @Query("UPDATE CsrfToken c SET c.token = :token, c.headerName = :headerName, c.parameterName = :parameterName, c.expiresAt = :expiresAt WHERE c.sessionId = :sessionId")
    int updateTokenBySessionId(String sessionId, String token, String headerName, String parameterName, LocalDateTime expiresAt);
}
