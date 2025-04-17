package com.sprint.mission.discodeit.Mapper;

import com.sprint.mission.discodeit.dto.response.ReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.stereotype.Component;

@Component
public class ReadStatusMapper {
    public ReadStatusDto toDto(ReadStatus readStatus) {
        return ReadStatusDto.builder()
                .id(readStatus.getId())
                .userId(readStatus.getUser().getId())
                .channelId(readStatus.getChannel().getId())
                .lastReadAt(readStatus.getLastReadAt())
                .build();
    }
}
