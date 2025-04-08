package com.sprint.mission.discodeit.exception.auth;

import java.util.NoSuchElementException;

public class PasswordMismatchException extends NoSuchElementException {

  public PasswordMismatchException(String message) {
    super(message);
  }
}
