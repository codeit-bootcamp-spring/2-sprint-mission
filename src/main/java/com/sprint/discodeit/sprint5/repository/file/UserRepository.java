package com.sprint.discodeit.sprint5.repository.file;

import com.sprint.discodeit.sprint5.domain.entity.User;
import com.sprint.discodeit.sprint5.repository.util.FindRepository;
import com.sprint.discodeit.sprint5.repository.util.SaveRepository;

public interface UserRepository extends SaveRepository<User>, FindRepository<User> {
}
