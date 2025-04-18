package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.service.user.userstatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.service.user.userstatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.service.user.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public interface UserStatusService {

  UserStatusDto create(UserStatusCreateRequest statusParam);

  UserStatusDto find(UUID id);

  List<UserStatusDto> findAll();

  UserStatus findByUserId(UUID userId);

  UserStatus update(UUID id, UserStatusUpdateRequest request);

  UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request);

  void delete(UUID id);
}
