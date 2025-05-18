package com.sprint.mission.discodeit.message.dto;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentResult;
import com.sprint.mission.discodeit.message.entity.Message;
import com.sprint.mission.discodeit.user.dto.UserResult;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageResult(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        UserResult author,
        String content,
        UUID channelId,
        List<BinaryContentResult> attachments
) {
    public static MessageResult fromEntity(Message message, UserResult author, List<BinaryContentResult> attachments) {
        return new MessageResult(message.getId(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                author,
                message.getContext(),
                message.getChannel().getId(),
                attachments);
    }

}
