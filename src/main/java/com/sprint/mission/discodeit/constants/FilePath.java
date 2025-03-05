package com.sprint.mission.discodeit.constants;

import java.nio.file.Path;
import java.nio.file.Paths;

public enum FilePath {
    STORAGE_DIRECTORY(Paths.get(System.getProperty("user.dir"),
            "src", "main", "java", "com", "sprint", "mission", "storage")),
    USER_FILE(STORAGE_DIRECTORY.path.resolve("user.ser"));

    private final Path path;

    FilePath(Path path) {
        this.path = path;
    }

    public Path getPath() {
        return path;
    }
}
