package com.sprint.mission.discodeit.exception.userstatus;

import java.util.NoSuchElementException;

public class UserStatusNotFoundException extends NoSuchElementException {

  public UserStatusNotFoundException(String message) {
    super(message);
  }
}
