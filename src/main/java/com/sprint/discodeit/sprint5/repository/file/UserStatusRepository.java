package com.sprint.discodeit.sprint5.repository.file;

import com.sprint.discodeit.sprint5.domain.entity.UserStatus;
import com.sprint.discodeit.sprint5.repository.util.FindRepository;
import com.sprint.discodeit.sprint5.repository.util.SaveRepository;

public interface UserStatusRepository extends FindRepository<UserStatus> , SaveRepository<UserStatus> {
}
