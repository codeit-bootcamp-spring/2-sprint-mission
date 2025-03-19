package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.constant.UserStatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class UserStatus extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID userUUID;

    @Builder.Default
    private Instant lastLoginTime = null;

    @Builder.Default
    private UserStatusType userStatusType = UserStatusType.NOTCONNECTIED;

    public void updateLastLoginTime() {
        lastLoginTime = ZonedDateTime.now(ZoneId.systemDefault()).toInstant();
    }

    public void updateLastStatus(UserStatusType userStatusType) {
        super.updateTime();
        this.userStatusType = userStatusType;
    }
}
