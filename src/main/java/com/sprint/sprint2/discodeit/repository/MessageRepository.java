package com.sprint.sprint2.discodeit.repository;

import com.sprint.sprint2.discodeit.entity.Message;
import com.sprint.sprint2.discodeit.repository.util.FindRepository;
import com.sprint.sprint2.discodeit.repository.util.SaveRepository;

public interface MessageRepository extends SaveRepository<Message>, FindRepository<Message> {
}
