package com.sprint.mission.discodeit.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserStatusType {
    ONLINE("온라인"),
    AWAY("자리비움"),
    BUSY("방해금지"),
    OFFLINE("오프라인");

    private final String displayName;
}
