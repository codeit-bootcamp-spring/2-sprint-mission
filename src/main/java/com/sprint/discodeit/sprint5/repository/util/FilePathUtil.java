package com.sprint.discodeit.sprint5.repository.util;

public enum FilePathUtil {

    CHANNELS("channels.dat"),
    usersS("userss.dat"),
    MESSAGE("message.dat"),
    STATUS("status.dat"),
    BINARY("binary.dat"),
    READSTATUS("reads.dat");

    private final String path;

    FilePathUtil(String path) {
        this.path = path;
    }

    public String getPath() {
        return path.toString();
    }
}
