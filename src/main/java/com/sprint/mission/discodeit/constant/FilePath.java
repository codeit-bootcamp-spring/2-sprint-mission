package com.sprint.mission.discodeit.constant;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FilePath {
    private FilePath() {
    }

    private static final String SER_EXTENSION = ".ser";
    public static final String JPG_EXTENSION = ".jpg";
    public static final Path STORAGE_DIRECTORY = Paths.get(System.getProperty("user.dir"), "filestorage");
    public static final Path CHANNEL_TEST_FILE = STORAGE_DIRECTORY.resolve("channel" + SER_EXTENSION);
    public static final Path MESSAGE_TEST_FILE = STORAGE_DIRECTORY.resolve("message" + SER_EXTENSION);
    public static final Path USER_TEST_FILE = STORAGE_DIRECTORY.resolve("user" + SER_EXTENSION);
    public static final Path IMAGE_STORAGE_DIRECTORY = STORAGE_DIRECTORY.resolve(Paths.get("images", "profile"));
    public static final Path USER_STATUS_TEST_FILE = STORAGE_DIRECTORY.resolve("userStatus" + SER_EXTENSION);
    public static final Path READ_STATUS_TEST_FILE = STORAGE_DIRECTORY.resolve("readStatus" + SER_EXTENSION);
    public static final Path BINARY_CONTENT_TEST_FILE = STORAGE_DIRECTORY.resolve("binaryContent" + SER_EXTENSION);
}
