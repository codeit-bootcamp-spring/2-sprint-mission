package com.sprint.mission.discodeit.mapper.Impl;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Comparator;

@Component
@RequiredArgsConstructor
public class ChannelMapperImpl implements ChannelMapper {
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;

    private final UserMapper userMapper;

    @Override
    public ChannelDto toDto(Channel channel) {
        return new ChannelDto(
                channel.getId(),
                channel.getType(),
                channel.getName(),
                channel.getDescription(),
                readStatusRepository.findAllByChannel_Id(channel.getId()).stream().map(ReadStatus::getUser).map(userMapper::toDto).toList(),
                messageRepository.findAllByChannelId(channel.getId()).stream().sorted(Comparator.comparing(Message::getCreatedAt).reversed())
                        .map(Message::getCreatedAt)
                        .limit(1)
                        .findFirst()
                        .orElse(Instant.MIN)
        );
    }
}