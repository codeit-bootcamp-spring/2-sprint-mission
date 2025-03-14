package com.sprint.mission.discodeit.constant;

public enum UserStatusType {
    CONNECTING("접속중"),
    NOTCONNECTIED("비접속");

    private final String status;

    UserStatusType(String status) {
        this.status = status;
    }
}
