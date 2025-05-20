package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.*;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring",
    uses = {UserMapper.class}
)
public abstract class ChannelMapper {

    @Autowired
    protected MessageService messageService;
    @Autowired
    protected UserMapper userMapper;

    @Autowired
    protected MessageRepository messageRepository;
    
    @Autowired
    protected ReadStatusRepository readStatusRepository;

    @Mapping(target = "lastMessageAt", expression = "java(getLastMessageTime(channel))")
    @Mapping(target = "participants", expression = "java(mapParticipants(channel))")
    public abstract ChannelDto toDto(Channel channel);

    public abstract List<ChannelDto> toDto(List<Channel> channels);

    public Instant getLastMessageTime(Channel channel) {
        return messageService.lastMessageTime(channel.getId());
    }

    protected Set<UserDto> mapParticipants(Channel channel) {
        return readStatusRepository.findAllByChannelId(channel.getId()).stream()
            .map(readStatus -> userMapper.toDto(readStatus.getUser()))
            .collect(Collectors.toSet());
    }
}
