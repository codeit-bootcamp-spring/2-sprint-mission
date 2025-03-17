package com.sprint.mission.discodeit.DTO.ReadStatus;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ReadStatusCRUDDTO(
        UUID readStatusId,
        UUID userId,
        UUID channelId
) {
    public static ReadStatusCRUDDTO create(
            UUID userId,
            UUID channelID
    ) {
        return ReadStatusCRUDDTO.builder()
                .userId(userId)
                .channelId(channelID).build();
    }

    public static ReadStatusCRUDDTO update(UUID readStatusId) {
        return ReadStatusCRUDDTO.builder().readStatusId(readStatusId).build();
    }

}
