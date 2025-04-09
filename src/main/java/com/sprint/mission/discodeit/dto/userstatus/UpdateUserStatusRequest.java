package com.sprint.mission.discodeit.dto.userstatus;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateUserStatusRequest {

    private Instant newLastActiveAt;
}
