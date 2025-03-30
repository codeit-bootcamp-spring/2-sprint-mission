package com.sprint.mission.discodeit.application.dto.message;

import com.sprint.mission.discodeit.application.dto.user.UserResult;
import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageResult(UUID messageId, String context, Instant createdAt, UUID channelId, List<UUID> attachmentIds,
                            UserResult user) {
    public static MessageResult fromEntity(Message message, UserResult userResult) {
        return new MessageResult(message.getId(), message.getContext(), message.getCreatedAt(), message.getChannelId(), message.getAttachmentIds(), userResult);
    }
}
