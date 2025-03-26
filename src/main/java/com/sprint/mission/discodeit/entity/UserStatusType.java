package com.sprint.mission.discodeit.entity;

import lombok.Getter;

@Getter
public enum UserStatusType {
    ONLINE("online"),
    OFFLINE("offline"),
    IDLE("idle"),
    DO_NOT_DISTURB("do_not_disturb"),
    INVISIBLE("invisible");
    private final String value;

    UserStatusType(String value) {
        this.value = value;
    }

    public static UserStatusType fromString(String status) {
        for (UserStatusType type : UserStatusType.values()) {
            if (type.value.equalsIgnoreCase(status)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + status);
    }
}
