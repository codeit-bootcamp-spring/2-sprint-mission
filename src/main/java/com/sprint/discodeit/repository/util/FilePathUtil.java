package com.sprint.discodeit.repository.util;

public enum FilePathUtil {

    CHANNELS("channels.dat"),
    USERS("users.dat"),
    Message("message.dat"),
    STATUS("status.dat"),
    BINARY("binary.dat");

    private final String path;

    FilePathUtil(String path) {
        this.path = path;
    }

    public String getPath() {
        return path.toString();
    }
}
