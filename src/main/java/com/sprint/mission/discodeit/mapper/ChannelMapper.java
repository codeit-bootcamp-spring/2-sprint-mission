package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.MessageJPARepository;
import com.sprint.mission.discodeit.repository.ReadStatusJPARepository;
import com.sprint.mission.discodeit.service.dto.response.ChannelResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ChannelMapper {

    private final MessageJPARepository messageJpaRepository;
    private final ReadStatusJPARepository readStatusJpaRepository;
    private final UserMapper userMapper;

    public ChannelResponseDto toDto(Channel channel) {
        Instant lastMessageAt = messageJpaRepository.findByChannel_Id(channel.getId()).stream()
                .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
                .map(Message::getCreatedAt)
                .limit(1)
                .findFirst()
                .orElse(Instant.MIN);

        List<User> user = (channel.getType().equals(ChannelType.PRIVATE))
                ? readStatusJpaRepository.findByChannel_Id(channel.getId()).stream()
                .map(ReadStatus::getUser)
                .toList() : null;

        return new ChannelResponseDto(
                channel.getId(),
                channel.getType(),
                channel.getName(),
                channel.getDescription(),
                user != null ? user.stream().map(userMapper::toDto).toList() : List.of(),
                lastMessageAt
        );
    }


    public ChannelResponseDto toDto1(Channel channel) {
        Instant lastMessageAt = messageJpaRepository.findByChannel_Id(channel.getId()).stream()
                .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
                .map(Message::getCreatedAt)
                .limit(1)
                .findFirst()
                .orElse(Instant.MIN);

        List<User> user = (channel.getType().equals(ChannelType.PRIVATE))
                ? readStatusJpaRepository.findByChannel_Id(channel.getId()).stream().map(ReadStatus::getUser).toList() : null;

        return ChannelResponseDto.builder()
                .id(channel.getId())
                .type(channel.getType())
                .name(channel.getName())
                .description(channel.getDescription())
                .participants(user != null ? user.stream().map(userMapper::toDto).toList() : List.of())
                .lastMessageAt(lastMessageAt)
                .build();

    }
}
