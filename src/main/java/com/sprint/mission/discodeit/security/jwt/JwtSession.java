package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtSession extends BaseUpdatableEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "uuid")
    private User user;

    @Column(nullable = false, length = 512)
    private String accessToken;

    @Column(nullable = false, length = 512)
    private String refreshToken;

    @Column(nullable = false, columnDefinition = "timestamp with time zone")
    private Instant issuedAt;

    @Column(nullable = false, columnDefinition = "timestamp with time zone")
    private Instant expiresAt;

    @Builder
    public JwtSession(User user, String accessToken, String refreshToken, Instant issuedAt,
        Instant expiresAt) {
        this.user = user;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
    }
}
