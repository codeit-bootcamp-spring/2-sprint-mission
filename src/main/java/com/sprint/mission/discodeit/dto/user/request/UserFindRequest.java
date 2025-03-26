package com.sprint.mission.discodeit.dto.user.request;

import com.sprint.mission.discodeit.domain.UserStatus;

import java.util.UUID;

public record UserFindRequest(
        UUID id,
        String username,
        String email,
        // 사용자의 온라인 상태 정보를 같이 포함하세요.
        UserStatus userStatus
        // 패스워드 정보는 제외하세요.
) {}
