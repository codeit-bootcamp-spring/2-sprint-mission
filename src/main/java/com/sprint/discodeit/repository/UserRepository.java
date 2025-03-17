package com.sprint.discodeit.repository;

import com.sprint.discodeit.entity.User;
import com.sprint.discodeit.repository.util.FindRepository;
import com.sprint.discodeit.repository.util.SaveRepository;

public interface UserRepository extends SaveRepository<User>, FindRepository<User> {
}
