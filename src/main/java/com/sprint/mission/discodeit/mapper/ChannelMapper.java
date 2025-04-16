package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelMapper {

    private final MessageService messageService;

    public ChannelDto toDto(Channel channel) {
        return new ChannelDto(
            channel.getId(),
            channel.getType(),
            channel.getName(),
            channel.getDescription(),
            channel.getParticipants(),
            messageService.lastMessageTime(channel.getId())
        );

    }

}
