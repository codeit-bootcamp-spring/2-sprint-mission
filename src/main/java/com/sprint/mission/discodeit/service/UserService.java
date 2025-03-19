package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.dto.UserCreateParam;
import com.sprint.mission.discodeit.service.dto.UserInfoResponse;
import com.sprint.mission.discodeit.service.dto.UserUpdateParam;
import java.util.List;
import java.util.UUID;

public interface UserService {
    User create(UserCreateParam createParam);

    UserInfoResponse find(UUID userId);

    List<UserInfoResponse> findAll();

    User update(UserUpdateParam updateParam);

    void delete(UUID userId);
}
