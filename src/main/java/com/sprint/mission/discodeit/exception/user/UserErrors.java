package com.sprint.mission.discodeit.exception.user;

import java.util.UUID;

public final class UserErrors {

  private UserErrors() {
  }

  public static final String USER_ALREADY_EXISTS_MESSAGE = "User with id %s already exists";
  public static final String USER_NOT_FOUND_MESSAGE = "User not found: %s";
  public static final String USER_LOGIN_FAILED_MESSAGE = "User with id %s login failed: %s";
  public static final String USER_REGISTER_FAILED_MESSAGE = "User register failed: %s";

  public static UserAlreadyExistsError userAlreadyExistsError(UUID id) {
    throw new UserAlreadyExistsError(String.format(USER_ALREADY_EXISTS_MESSAGE, id));
  }

  public static UserNotFoundError userNotFoundError(UUID id) {
    throw new UserNotFoundError(String.format(USER_NOT_FOUND_MESSAGE, id));
  }

  public static UserLoginFailedError userLoginFailedError(UUID id, String details) {
    throw new UserLoginFailedError(String.format(USER_LOGIN_FAILED_MESSAGE, id, details));
  }

  public static UserRegisterFailedError userRegisterFailedError(String details) {
    throw new UserRegisterFailedError(String.format(USER_REGISTER_FAILED_MESSAGE, details));
  }

}
