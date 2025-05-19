package com.sprint.mission.discodeit.domain.message.mapper;

import com.sprint.mission.discodeit.domain.binarycontent.dto.BinaryContentResult;
import com.sprint.mission.discodeit.domain.message.dto.MessageResult;
import com.sprint.mission.discodeit.domain.message.entity.Message;
import com.sprint.mission.discodeit.domain.user.dto.UserResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@RequiredArgsConstructor
@Component
public class MessageResultMapper {

    public MessageResult convertToMessageResult(Message message) {
        return MessageResult.fromEntity(message,
                UserResult.fromEntity(message.getUser(), message.getUser().getUserStatus().isOnline(Instant.now())),
                BinaryContentResult.fromEntity(message.getAttachments()));
    }

}
