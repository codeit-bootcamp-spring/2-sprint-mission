package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelMapper {

    // TODO 매개변수로 channel 넘겨주면 DTO로 변환하는 로직으로 변경
    public ChannelDto toDto(Channel channel, List<UserDto> participants, Instant lastMessageAt) {

        ChannelDto dto = new ChannelDto(
            channel.getId(),
            channel.getChannelType(),
            channel.getName(),
            channel.getDescription(),
            participants,
            lastMessageAt
        );

        return dto;
    }

}
