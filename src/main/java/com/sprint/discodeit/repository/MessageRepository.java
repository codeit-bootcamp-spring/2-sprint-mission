package com.sprint.discodeit.repository;

import com.sprint.discodeit.entity.Message;
import com.sprint.discodeit.repository.util.FindRepository;
import com.sprint.discodeit.repository.util.SaveRepository;

public interface MessageRepository extends SaveRepository<Message>, FindRepository<Message> {
}
