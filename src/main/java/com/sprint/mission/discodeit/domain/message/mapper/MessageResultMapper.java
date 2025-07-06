package com.sprint.mission.discodeit.domain.message.mapper;

import com.sprint.mission.discodeit.domain.binarycontent.dto.BinaryContentResult;
import com.sprint.mission.discodeit.domain.message.dto.MessageResult;
import com.sprint.mission.discodeit.domain.message.entity.Message;
import com.sprint.mission.discodeit.domain.user.mapper.UserResultMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MessageResultMapper {

  private final UserResultMapper userResultMapper;

  public MessageResult convertToMessageResult(Message message) {
    return MessageResult.fromEntity(message,
        userResultMapper.convertToUserResult(message.getUser()),
        BinaryContentResult.fromEntity(message.getAttachments()));
  }

}
