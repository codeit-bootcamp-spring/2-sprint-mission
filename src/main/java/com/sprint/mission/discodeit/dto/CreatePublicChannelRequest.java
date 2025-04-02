package com.sprint.mission.discodeit.dto;

import lombok.Data;

@Data
public class CreatePublicChannelRequest {
    private String channelName;
    private String description;
}
