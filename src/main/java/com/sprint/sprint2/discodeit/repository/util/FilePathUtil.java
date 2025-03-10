package com.sprint.sprint2.discodeit.repository.util;

public enum FilePathUtil {

    CHANNELS("channels.dat"),
    USERS("users.dat"),
    Message("message.dat");

    private final String path;

    FilePathUtil(String path) {
        this.path = path;
    }

    public String getPath() {
        return path.toString();
    }
}
