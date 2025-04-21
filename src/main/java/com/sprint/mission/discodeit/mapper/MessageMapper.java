package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageMapper {

  private final BinaryContentMapper binaryContentMapper;
  private final UserMapper userMapper;
  private final BinaryContentStorage binaryContentStorage;

  public MessageDto toDto(Message message) {
    return new MessageDto(
        message.getId(),
        message.getCreatedAt(),
        message.getUpdatedAt(),
        message.getContent(),
        message.getChannel().getId(),
        userMapper.toDto(message.getAuthor()),
        attachments(message)
    );
  }

  private List<BinaryContentDto> attachments(Message message) {
    return message.getAttachments().stream()
        .map(binaryContent -> binaryContentMapper.toDto(binaryContent,
            binaryContentStorage.get(binaryContent.getId())))
        .toList();
  }
}
