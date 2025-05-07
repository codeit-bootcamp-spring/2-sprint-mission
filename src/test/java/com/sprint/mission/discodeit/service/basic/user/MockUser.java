package com.sprint.mission.discodeit.service.basic.user;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;

public final class MockUser {

    private MockUser() {
    }

    public static UserCreateRequest createMockUserRequest(String name, String email) {
        return new UserCreateRequest(name, email, UserInfo.LOGIN_USER.getPassword());
    }
}
