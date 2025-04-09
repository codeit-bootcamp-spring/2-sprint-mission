package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageResponseDto {

    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;

    private String content;
    private UUID authorId;
    private UUID channelId;
    private List<UUID> attachmentIds;

    public static MessageResponseDto from(Message message) {
        return new MessageResponseDto(
            message.getId(),
            message.getCreatedAt(),
            message.getUpdatedAt(),
            message.getContent(),
            message.getAuthorId(),
            message.getChannelId(),
            message.getAttachedImageIds()
        );
    }
}
