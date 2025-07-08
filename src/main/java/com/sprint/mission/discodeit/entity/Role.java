package com.sprint.mission.discodeit.entity;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_USER("일반 사용자", 3),
    ROLE_CHANNEL_MANAGER("채널 매니저", 2),
    ROLE_ADMIN("관리자", 1);

    private final String description;
    private final int hierarchy;

    Role(String description, int hierarchy) {
        this.description = description;
        this.hierarchy = hierarchy;
    }

}