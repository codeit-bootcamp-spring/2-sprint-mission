package com.sprint.mission.discodeit.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MessageUpdateRequest {
    private String newContent;

    @Builder
    public MessageUpdateRequest(String newContent) {
        this.newContent = newContent;
    }
}
