package com.sprint.mission.discodeit.util.mock.user;

import com.sprint.mission.discodeit.application.dto.user.UserCreateRequest;

public class MockUser {

  public static UserCreateRequest createMockUserRequest(String name, String email) {
    return new UserCreateRequest(name, email, SetUpUserInfo.LOGIN_USER.getPassword());
  }
}
