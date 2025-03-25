package com.sprint.mission.discodeit.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PublicChannelUpdateRequest {
    private String newName;
    private String newDescription;

    @Builder
    public PublicChannelUpdateRequest(String newName, String newDescription) {
        this.newName = newName;
        this.newDescription = newDescription;
    }
}
