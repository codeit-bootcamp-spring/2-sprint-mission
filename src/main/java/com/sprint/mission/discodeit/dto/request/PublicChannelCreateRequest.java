package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Public Channel 생성 정보")
public record PublicChannelCreateRequest(
    String name,
    String description
) {

}
