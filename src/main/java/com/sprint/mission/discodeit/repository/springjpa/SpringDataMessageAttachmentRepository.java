package com.sprint.mission.discodeit.repository.springjpa;

import com.sprint.mission.discodeit.entity.MessageAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public interface SpringDataMessageAttachmentRepository extends JpaRepository<MessageAttachment, UUID> {
    List<MessageAttachment> findAllByMessageId(UUID messageId);
}
