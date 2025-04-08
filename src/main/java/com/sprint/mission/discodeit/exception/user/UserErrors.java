package com.sprint.mission.discodeit.exception.user;

import java.util.UUID;

public final class UserErrors {

  private UserErrors() {
  }

  public static final String USER_NAME_ALREADY_EXISTS_MESSAGE = "User with name %s already exists";
  public static final String USER_EMAIL_ALREADY_EXISTS_MESSAGE = "User with email %s already exists";
  public static final String USER_NOT_FOUND_MESSAGE = "User not found: %s";
  public static final String USER_LOGIN_FAILED_MESSAGE = "User with id %s login failed: %s";
  public static final String NULL_POINT_USER_ID = "User ID is null";

  public static UserAlreadyExistsError userNameAlreadyExistsError(String name) {
    throw new UserAlreadyExistsError(String.format(USER_NAME_ALREADY_EXISTS_MESSAGE, name));
  }

  public static UserAlreadyExistsError userEmailAlreadyExistsError(String email) {
    throw new UserAlreadyExistsError(String.format(USER_EMAIL_ALREADY_EXISTS_MESSAGE, email));
  }

  public static UserNotFoundError userNameNotFoundError(String name) {
    throw new UserNotFoundError(String.format(USER_NOT_FOUND_MESSAGE, name));
  }

  public static UserNotFoundError userIdNotFoundError(UUID id) {
    throw new UserNotFoundError(String.format(USER_NOT_FOUND_MESSAGE, id));
  }

  public static UserLoginFailedError userLoginFailedError(UUID id, String details) {
    throw new UserLoginFailedError(String.format(USER_LOGIN_FAILED_MESSAGE, id, details));
  }

  public static NullPointUserIdError nullPointUserIdError() {
    throw new NullPointUserIdError(NULL_POINT_USER_ID);
  }


}
