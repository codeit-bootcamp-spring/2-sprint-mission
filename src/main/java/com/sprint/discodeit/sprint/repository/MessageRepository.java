package com.sprint.discodeit.sprint.repository;

import com.sprint.discodeit.sprint.domain.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
