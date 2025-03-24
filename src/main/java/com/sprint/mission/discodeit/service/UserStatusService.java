package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusResponse;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateByUserIdRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusService {
    UserStatusResponse create(UserStatusCreateRequest request);
    Optional<UserStatusResponse> find(UUID id);
    List<UserStatusResponse> findAll();
    void update(UserStatusUpdateRequest request);
    void updateByUserId(UserStatusUpdateByUserIdRequest request);
    void delete(UUID id);
}
