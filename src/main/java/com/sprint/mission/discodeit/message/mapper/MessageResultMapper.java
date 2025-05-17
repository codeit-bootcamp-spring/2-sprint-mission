package com.sprint.mission.discodeit.message.mapper;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentResult;
import com.sprint.mission.discodeit.message.dto.MessageResult;
import com.sprint.mission.discodeit.message.entity.Message;
import com.sprint.mission.discodeit.user.dto.UserResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
@Component
public class MessageResultMapper {

    public MessageResult convertToMessageResult(Message message) {
        List<BinaryContentResult> attachments = message.getAttachments()
                .stream()
                .map(BinaryContentResult::fromEntity)
                .toList();


        return MessageResult.fromEntity(message,
                UserResult.fromEntity(message.getUser(), message.getUser().getUserStatus().isOnline(Instant.now())),
                attachments);
    }

}
