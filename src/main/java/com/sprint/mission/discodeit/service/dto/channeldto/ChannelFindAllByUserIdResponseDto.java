package com.sprint.mission.discodeit.service.dto.channeldto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record ChannelFindAllByUserIdResponseDto(
        UUID id,
        ChannelType channelType,
        String name,
        String description,
        Instant createdAt

) {
    public static List<ChannelFindAllByUserIdResponseDto> fromChannel(List<Channel> channel) {
        return channel.stream()
                .map(c -> new ChannelFindAllByUserIdResponseDto(
                        c.getId(),
                        c.getType(),
                        c.getChannelName(),
                        c.getDescription(),
                        c.getCreatedAt()
                ))  // Channel 객체를 ChannelFindAllByUserIdResponseDto로 변환
                .toList();
    }

}