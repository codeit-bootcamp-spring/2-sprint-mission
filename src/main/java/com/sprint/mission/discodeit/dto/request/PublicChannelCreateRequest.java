package com.sprint.mission.discodeit.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PublicChannelCreateRequest {
    private String name;
    private String description;

    @Builder
    public PublicChannelCreateRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
