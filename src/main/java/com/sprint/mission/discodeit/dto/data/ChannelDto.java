package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.ChannelType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Schema(description = "채널 응답 DTO")
public record ChannelDto(
        @Schema(description = "채널 ID")
        UUID id,

        @Schema(description = "채널 타입")
        ChannelType type,

        @Schema(description = "채널명")
        String name,

        @Schema(description = "채널 설명")
        String description,

        @Schema(description = "참여자 목록")
        List<UserDto> participants,

        @Schema(description = "마지막 메시지 시간")
        Instant lastMessageAt
) {}
