package com.sprint.mission.discodeit.dto.user;

import java.util.UUID;

//사용자 조회 응답 전달 DTO (find, findAll) -> 아이디, 온라인 여부 (아이디도 필요 없나?)
public record UserIsOnlineResponse(
        UUID userId,
        boolean isOnline // 온라인 상태
) {}
