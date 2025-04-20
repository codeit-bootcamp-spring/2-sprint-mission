package com.sprint.mission.discodeit.application.dto.message;

import com.sprint.mission.discodeit.application.dto.binarycontent.BinaryContentResult;
import com.sprint.mission.discodeit.application.dto.user.UserResult;
import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageResult(UUID id,
                            Instant createdAt,
                            Instant updatedAt,
                            UserResult author,
                            String content,
                            UUID channelId,
                            List<BinaryContentResult> attachments) {
    public static MessageResult fromEntity(Message message, UserResult author) {
        List<BinaryContentResult> attachments = message.getAttachments()
                .stream()
                .map(BinaryContentResult::fromEntity)
                .toList();

        return new MessageResult(message.getId(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                author,
                message.getContext(),
                message.getChannel().getId(),
                attachments);
    }
}
