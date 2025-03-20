package com.sprint.mission.discodeit.dto.user.response;

import com.sprint.mission.discodeit.domain.UserStatus;

public record UserFindResponse(
    String username,
    String email,
    UserStatus userStatus
) {}
