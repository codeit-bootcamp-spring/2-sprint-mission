package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateByUserIdDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatus create(UserStatusCreateDto userStatusCreateDto);

    UserStatus findById(UUID userStatusId);

    List<UserStatus> findAll();

    UserStatus update(UserStatusUpdateDto userStatusUpdateDto);

    UserStatus updateByUserId(UserStatusUpdateByUserIdDto userStatusUpdateByUserIdDto);

    void delete(UUID userStatusId);
}
