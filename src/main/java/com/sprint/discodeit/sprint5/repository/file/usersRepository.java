package com.sprint.discodeit.sprint5.repository.file;

import com.sprint.discodeit.sprint5.domain.entity.users;
import com.sprint.discodeit.sprint5.repository.util.FindRepository;
import com.sprint.discodeit.sprint5.repository.util.SaveRepository;

public interface usersRepository extends SaveRepository<users>, FindRepository<users> {
}
