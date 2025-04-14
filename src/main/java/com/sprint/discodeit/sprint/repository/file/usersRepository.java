package com.sprint.discodeit.sprint.repository.file;

import com.sprint.discodeit.sprint.domain.entity.users;
import com.sprint.discodeit.sprint.repository.util.FindRepository;
import com.sprint.discodeit.sprint.repository.util.SaveRepository;

public interface usersRepository extends SaveRepository<users>, FindRepository<users> {
}
