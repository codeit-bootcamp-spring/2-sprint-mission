package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageMapper {

  public MessageDto toDto(Message message) {
    if (message == null) return null;

    return new MessageDto(
        message.getId(),
        message.getContent(),
        message.getChannel() != null ? message.getChannel().getId() : null,
        message.getAuthor() != null ? message.getAuthor().getId() : null,
        message.getAttachmentIds() != null
            ? message.getAttachmentIds().stream()
            .map(BinaryContent::getId)
            .toList()
            : List.of(),
        message.getCreatedAt(),
        message.getUpdatedAt()
    );
  }
}