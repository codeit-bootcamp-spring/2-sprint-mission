package com.sprint.mission.discodeit.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class ChannelUpdateRequestDto {
    private UUID channelId;
    private String newName;
    private String newDescription;

    public ChannelUpdateRequestDto(UUID channelId, String newName, String newDescription) {
        this.channelId = channelId;
        this.newName = newName;
        this.newDescription = newDescription;
    }
}
