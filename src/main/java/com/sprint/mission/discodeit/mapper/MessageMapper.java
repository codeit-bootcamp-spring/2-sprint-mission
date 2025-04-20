package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageMapper {

  BinaryContentMapper binaryContentMapper;
  UserMapper userMapper;

  public MessageDto toDto(Message message) {
    if (message == null) {
      return null;
    }

    List<BinaryContentDto> attachments = null;
    if (message.getAttachments() != null) {
      attachments = message.getAttachments().stream()
          .map(binaryContentMapper::toDto)
          .toList();
    }

    UserDto authorDto = null;
    if (message.getAuthor() != null) {
      authorDto = userMapper.toDto(message.getAuthor());
    }

    return MessageDto.builder()
        .id(message.getId())
        .createdAt(message.getCreatedAt())
        .updatedAt(message.getUpdatedAt())
        .content(message.getText())
        .channelId(message.getChannel().getId())
        .author(authorDto)
        .attachments(attachments)
        .build();
  }

  public Message toEntity(MessageCreateRequest request) {
    if (request == null) {
      return null;
    }

    Message message = new Message();
    message.setText(request.content());

    return message;
  }
}
