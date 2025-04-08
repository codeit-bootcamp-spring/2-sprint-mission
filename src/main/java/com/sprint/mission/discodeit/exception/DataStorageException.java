package com.sprint.mission.discodeit.exception;

import java.util.*;

public class DataStorageException extends RuntimeException {

  public DataStorageException(String message) {
    super(message);
  }

  public DataStorageException(String message, Throwable cause) {
    super(message, cause);
  }
}
