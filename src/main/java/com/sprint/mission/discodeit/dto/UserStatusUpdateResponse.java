package com.sprint.mission.discodeit.dto;

public record UserStatusUpdateResponse(boolean success, boolean online) {
    public static UserStatusUpdateResponse of(boolean success, boolean online) {
        return new UserStatusUpdateResponse(success, online);
    }
}
