package com.sprint.mission.discodeit.constant;

import lombok.Getter;

@Getter
public enum SubDirectory {
    USER("user"),
    CHANNEL("channel"),
    MESSAGE("message"),
    USER_STATUS("userstatus"),
    PROFILE("profile"),
    FILE("file");

    private final String directory;

    SubDirectory(String directory) {
        this.directory = directory;
    }
}
