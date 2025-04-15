package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class UserStatus extends BaseUpdatableEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true, nullable = false)
    // 외래키 소유(주인)
    private User user;

    @Column(nullable = false)
    private Instant lastActiveAt;


    public UserStatus(User user) {
        super();
        this.user = user;
        this.lastActiveAt = Instant.now();
    }

    public UserStatus(User user, Instant lastActiveAt) {
        super();
        this.user = user;
        this.lastActiveAt = lastActiveAt;
    }

    // update 메서드 수정
    public boolean update(Instant newLastActiveAt) {
        boolean anyValueUpdated = false;
        // lastActiveAt이 null이 아니고, 이전 값과 다를 때만 업데이트
        if (newLastActiveAt != null && !newLastActiveAt.equals(this.lastActiveAt)) {
            this.lastActiveAt = newLastActiveAt;
            anyValueUpdated = true;
        }
        // updatedAt은 자동으로 처리됨
        return anyValueUpdated;
    }

    // isOnline 메서드는 그대로 유지 (JPA와 직접 관련 없음)
    @Transient // DB 컬럼으로 매핑하지 않음
    public Boolean isOnline() {
        if (this.lastActiveAt == null) {
            return false; // null 체크 추가
        }
        Instant instantFiveMinutesAgo = Instant.now().minus(Duration.ofMinutes(5));
        return lastActiveAt.isAfter(instantFiveMinutesAgo);
    }
}
