package com.sprint.discodeit.sprint5.repository.file;

import com.sprint.discodeit.sprint5.domain.entity.Message;
import com.sprint.discodeit.sprint5.repository.util.FindRepository;
import com.sprint.discodeit.sprint5.repository.util.SaveRepository;

public interface MessageRepository extends SaveRepository<Message>, FindRepository<Message> {
}
