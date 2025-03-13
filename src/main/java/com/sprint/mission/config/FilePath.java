package com.sprint.mission.config;

import java.nio.file.Path;
import java.nio.file.Paths;

public enum FilePath {
    STORAGE_DIRECTORY(Paths.get(System.getProperty("user.dir"),
            "src", "main", "java", "com", "sprint", "mission", "filestore")),
    USER_FILE(STORAGE_DIRECTORY.path.resolve("user.ser")),
    CHANNEL_FILE(STORAGE_DIRECTORY.path.resolve("channel.ser")),
    MESSAGE_FILE(STORAGE_DIRECTORY.path.resolve("message.ser"));

    private final Path path;

    FilePath(Path filePath) {
        this.path = filePath;
    }

    public Path getPath() {
        return path;
    }
}
