package com.sprint.mission.discodeit.constant;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FilePath {

  private FilePath() {
  }

  public static final String SER_EXTENSION = ".ser";
  public static final String JPG_EXTENSION = ".jpg";
  public static final Path STORAGE_DIRECTORY = Paths.get(System.getProperty("user.dir"),
      "filestorage");
  public static final Path IMAGE_STORAGE_DIRECTORY = STORAGE_DIRECTORY.resolve(
      Paths.get("images", "profile"));
}
