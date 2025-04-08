package com.sprint.mission.discodeit.exception.status.user;

import java.util.UUID;

public final class UserStatusErrors {

  private UserStatusErrors() {
  }

  public static final String USER_STATUS_NOT_FOUND = "User status not found: %s";
  public static final String USER_STATUS_ALREADY_EXISTS = "User status %s already exists";
  public static final String NULL_POINT_USER_STATUS_ID = "User Status ID is null";


  public static UserStatusNotFoundError userStatusIdNotFoundError(UUID id) {
    throw new UserStatusNotFoundError(String.format(USER_STATUS_NOT_FOUND, id));
  }

  public static UserStatusAlreadyExistsError userStatusAlreadyExistsError(UUID id) {
    throw new UserStatusAlreadyExistsError(String.format(USER_STATUS_ALREADY_EXISTS, id));
  }

  public static NullPointUserStatusIdError nullPointUserStatusIdError() {
    throw new NullPointUserStatusIdError(NULL_POINT_USER_STATUS_ID);
  }
}
