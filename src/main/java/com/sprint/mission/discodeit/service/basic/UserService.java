package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.user.response.UserCreateResponse;
import com.sprint.mission.discodeit.dto.user.response.UserFindResponse;
import com.sprint.mission.discodeit.dto.user.response.UserUpdateResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserCreateResponse create(UserCreateRequest userCreateRequest);
    UserFindResponse find(UUID userId);
    List<UserFindResponse> findAll();
    UserUpdateResponse update(UserUpdateRequest updateDTO);
    void delete(UUID userId);
}
