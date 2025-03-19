package com.sprint.mission.discodeit.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ChannelUpdateDto {
    UUID channelId;
    String channelName;
    String description;
}
