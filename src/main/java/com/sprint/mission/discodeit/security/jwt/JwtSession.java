package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
@Table(name = "jwt_sessions")
public class JwtSession extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "access_token", nullable = false, columnDefinition = "TEXT")
    private String accessToken;

    @Column(name = "refresh_token", nullable = false, columnDefinition = "TEXT")
    private String refreshToken;


    public void updateTokens(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
} 