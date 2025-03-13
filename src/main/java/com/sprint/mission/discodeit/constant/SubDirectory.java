package com.sprint.mission.discodeit.constant;

import lombok.Getter;

@Getter
public enum SubDirectory {
    USER("user"),
    CHANNEL("channel"),
    MESSAGE("message");

    private final String directory;

    SubDirectory(String directory) {
        this.directory = directory;
    }
}
