package com.sprint.mission.discodeit.mapper.Impl;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import org.springframework.stereotype.Component;

@Component
public class ReadStatusMapperImpl implements ReadStatusMapper {
    @Override
    public ReadStatusDto toDto(ReadStatus readStatus) {
        return new ReadStatusDto(
                readStatus.getId(),
                readStatus.getUser().getId(),
                readStatus.getChannel().getId(),
                readStatus.getLastReadAt()
        );
    }
}