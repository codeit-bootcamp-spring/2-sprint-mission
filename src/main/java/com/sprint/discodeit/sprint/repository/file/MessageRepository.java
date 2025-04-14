package com.sprint.discodeit.sprint.repository.file;

import com.sprint.discodeit.sprint.domain.entity.Message;
import com.sprint.discodeit.sprint.repository.util.FindRepository;
import com.sprint.discodeit.sprint.repository.util.SaveRepository;

public interface MessageRepository extends SaveRepository<Message>, FindRepository<Message> {
}
