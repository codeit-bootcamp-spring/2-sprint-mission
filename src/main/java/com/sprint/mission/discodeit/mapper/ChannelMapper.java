package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.MessageService;
import java.time.Instant;
import org.mapstruct.*;
import java.util.List;

// 추상클래스는 오토 주입 x
@Mapper(componentModel = "spring")
public abstract class ChannelMapper {

    private MessageService messageService;


    @Mapping(target = "lastMessageAt",
        expression = "java(getLastMessageTime(channel))")
    public abstract ChannelDto toDto(Channel channel);


    public abstract List<ChannelDto> toDto(List<Channel> channels);


    protected Instant getLastMessageTime(Channel channel) {
        return messageService.lastMessageTime(channel.getId());
    }
}
