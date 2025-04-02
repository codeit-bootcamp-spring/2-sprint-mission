package com.sprint.discodeit.repository;

import com.sprint.discodeit.domain.entity.UserStatus;
import com.sprint.discodeit.repository.util.FindRepository;
import com.sprint.discodeit.repository.util.SaveRepository;

public interface UserStatusRepository extends FindRepository<UserStatus> , SaveRepository<UserStatus> {
}
