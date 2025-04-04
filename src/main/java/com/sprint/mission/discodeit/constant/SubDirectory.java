package com.sprint.mission.discodeit.constant;

import lombok.Getter;

@Getter
public enum SubDirectory {
  USER("user"),
  CHANNEL("channel"),
  MESSAGE("message"),
  USER_STATUS("userstatus"),
  READ_STATUS("readstatus"),
  BINARY_CONTENT("binarycontent");

  private final String directory;

  SubDirectory(String directory) {
    this.directory = directory;
  }
}
