package com.sprint.mission.discodeit.core.user.usecase.status;

import com.sprint.mission.discodeit.core.user.entity.UserStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public interface UserStatusService {

  UserStatus create(UUID userId);

  UserStatus findByUserId(UUID userId);

  UserStatus findByStatusId(UUID userStatusId);

  List<UserStatus> findAll();

//  UUID update(UUID userId);

  void deleteById(UUID userStatusId);

  void deleteByUserId(UUID userId);
}
