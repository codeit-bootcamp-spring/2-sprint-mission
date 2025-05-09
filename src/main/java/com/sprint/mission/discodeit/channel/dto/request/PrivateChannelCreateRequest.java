package com.sprint.mission.discodeit.channel.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@Schema(description = "Private 채널 생성 요청")
public record PrivateChannelCreateRequest(List<UUID> participantIds) {
}
