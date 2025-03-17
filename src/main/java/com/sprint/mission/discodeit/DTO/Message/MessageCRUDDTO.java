package com.sprint.mission.discodeit.DTO.Message;

import com.sprint.mission.discodeit.entity.BinaryContent;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record MessageCRUDDTO(
        UUID serverId,
        UUID channelId,
        UUID messageId,
        UUID creatorId,
        String text,
        List<BinaryContent> binaryContent
) {
    public static MessageCRUDDTO create(UUID creatorId,
                                        UUID channelId,
                                        String text,
                                        List<BinaryContent> binaryContent) {
        return MessageCRUDDTO.builder()
                .creatorId(creatorId)
                .channelId(channelId)
                .text(text)
                .binaryContent(binaryContent).build();
    }

    public static MessageCRUDDTO delete(UUID serverId,
                                        UUID channelId,
                                        UUID messageId) {
        return MessageCRUDDTO.builder()
                .serverId(serverId)
                .channelId(channelId)
                .messageId(messageId).build();
    }

    public static MessageCRUDDTO update(UUID replaceId,
                                        String replaceText) {

        return MessageCRUDDTO.builder()
                .messageId(replaceId)
                .text(replaceText).build();
    }

}
