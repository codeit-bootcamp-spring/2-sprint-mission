package com.sprint.mission.discodeit.service.dto.user;

import com.sprint.mission.discodeit.entity.UserStatusType;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserInfoResponse {
    private final UUID userId;
    private final Instant createAt;
    private final Instant updateAt;
    private final String username;
    private final String email;
    private UUID profileId;
    private final UserStatusType status;
}
