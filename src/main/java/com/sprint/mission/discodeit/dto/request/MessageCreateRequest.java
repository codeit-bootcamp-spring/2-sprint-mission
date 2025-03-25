package com.sprint.mission.discodeit.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public class MessageCreateRequest {
    private String content;
    private UUID channelId;
    private UUID authorId;

    @Builder
    public MessageCreateRequest(String content, UUID channelId, UUID authorId) {
        this.content = content;
        this.channelId = channelId;
        this.authorId = authorId;
    }
}
