package com.sprint.mission.discodeit.service.dto.user;

import com.sprint.mission.discodeit.entity.UserStatusType;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;


public record UserInfoResponse (
        UUID userId,
        Instant createAt,
        Instant updateAt,
        String username,
        String email,
        UUID profileId,
        UserStatusType status
){
}