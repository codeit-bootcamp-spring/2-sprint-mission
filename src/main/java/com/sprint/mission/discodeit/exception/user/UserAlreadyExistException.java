package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class UserAlreadyExistException extends UserException {

  public UserAlreadyExistException byUserId(UUID userId) {
    return new UserAlreadyExistException(ErrorCode.DUPLICATE_USER, Map.of("userId", userId));
  }

  public static UserAlreadyExistException byEmail(String email) {
    return new UserAlreadyExistException(ErrorCode.DUPLICATE_USER_EMAIL, Map.of("email", email));
  }

  public static UserAlreadyExistException byUsername(String username) {
    return new UserAlreadyExistException(ErrorCode.DUPLICATE_USER_USERNAME,
        Map.of("username", username));
  }

  private UserAlreadyExistException(ErrorCode code, Map<String, Object> details) {
    super(code, details);
  }

}
