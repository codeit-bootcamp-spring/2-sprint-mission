package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ChannelMapper {
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final UserMapper userMapper;

    public ChannelDto toDto(Channel channel) {
        Instant lastMessageAt = messageRepository.findLastMessageAtByChannelId(channel.getId())
                .orElse(Instant.MIN);

        boolean isPrivate = channel.getType().equals(ChannelType.PRIVATE);
        List<UserDto> participants = isPrivate ?
                readStatusRepository.findAllByChannelIdWithUser(channel.getId()).stream()
                        .map(ReadStatus::getUser)
                        .map(userMapper::toDto)
                        .toList()
                : List.of();

        return new ChannelDto(channel, lastMessageAt, participants);
    }
}
