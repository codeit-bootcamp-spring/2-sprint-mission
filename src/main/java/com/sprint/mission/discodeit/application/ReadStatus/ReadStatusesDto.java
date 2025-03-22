package com.sprint.mission.discodeit.application.ReadStatus;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;

public record ReadStatusesDto(List<ReadStatusDto> readStatuses) {
    public static ReadStatusesDto fromEntity(List<ReadStatus> readStatuses) {
        List<ReadStatusDto> readStatusDtos = readStatuses.stream()
                .map(ReadStatusDto::fromEntity)
                .toList();

        return new ReadStatusesDto(readStatusDtos);
    }
}
