package com.sprint.mission.discodeit.constant;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FilePath {
    private FilePath() {
    }

    private static final String SER_EXTENSION = ".ser";
    public static final Path STORAGE_DIRECTORY = Paths.get(System.getProperty("user.dir"), "filestorage");
    public static final String CHANNEL_FILE = "channel" + SER_EXTENSION;
    public static final String MESSAGE_FILE = "message" + SER_EXTENSION;
    public static final String USER_FILE = "user" + SER_EXTENSION;
}
