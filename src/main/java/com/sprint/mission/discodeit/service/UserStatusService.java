package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.dto.service.user.userstatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.service.user.userstatus.UserStatusUpdateDto;
import com.sprint.mission.discodeit.dto.service.user.userstatus.UserStatusUpdateRequest;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public interface UserStatusService {

  UserStatus create(UserStatusCreateRequest statusParam);

  UserStatus find(UUID id);

  List<UserStatus> findAll();

  UserStatus findByUserId(UUID userId);

  UserStatus update(UUID id, UserStatusUpdateRequest request);

  UserStatusUpdateDto updateByUserId(UUID userId, UserStatusUpdateRequest request);

  void delete(UUID id);
}
