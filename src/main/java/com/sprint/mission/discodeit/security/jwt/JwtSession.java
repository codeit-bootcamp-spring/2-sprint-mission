package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "jwt_sessions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class JwtSession extends BaseUpdatableEntity {

    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID userId;
    @Column(columnDefinition = "VARCHAR(512)", nullable = false, unique = true)
    private String accessToken;
    @Column(columnDefinition = "VARCHAR(512)", nullable = false, unique = true)
    private String refreshToken;
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE", nullable = false)
    private Instant expirationTime;

    public JwtSession(UUID userId, String accessToken, String refreshToken,
        Instant expirationTime) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expirationTime = expirationTime;
    }

    public boolean isExpired() {
        return this.expirationTime.isBefore(Instant.now());
    }

    public void update(String accessToken, String refreshToken, Instant expirationTime) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expirationTime = expirationTime;
    }
}
