package com.sprint.mission.discodeit.util.mock.user;

import static com.sprint.mission.discodeit.util.mock.user.SetUpUserInfo.LOGIN_USER;

import com.sprint.mission.discodeit.application.dto.user.UserCreateRequest;

public class MockUser {

  public static UserCreateRequest createMockUserRequest(String name, String email) {
    return new UserCreateRequest(name, email, LOGIN_USER.getPassword());
  }
}
