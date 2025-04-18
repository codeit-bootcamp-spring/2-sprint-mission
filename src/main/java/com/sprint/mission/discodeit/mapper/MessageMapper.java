package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.entity.message.Message;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageMapper {

  private final UserMapper userMapper;
  private final BinaryContentMapper binaryContentMapper;

  public MessageDto toResponse(Message message) {
    List<BinaryContentDto> attachments = message.getAttachments().stream()
        .map(binaryContentMapper::toDto)
        .toList();

    return new MessageDto(
        message.getId(),
        message.getCreatedAt(),
        message.getUpdatedAt(),
        message.getContent(),
        message.getChannel().getId(),
        userMapper.toResponse(message.getAuthor()),
        attachments
    );
  }
}