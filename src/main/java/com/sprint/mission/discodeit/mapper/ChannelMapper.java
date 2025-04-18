package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelMapper {

    private final UserMapper userMapper;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;

    public ChannelDto toDto(Channel channel) {
        if (channel == null) {
            return null;
        }

        // 찾고 있는 채널에 참여 중인 사용자들의 readstauts 추출
        List<ReadStatus> readStatuses = readStatusRepository.findAll().stream()
            .filter(rs -> rs.getChannel().equals(channel))
            .toList();

        // 사용자들의 readstatus에서 사용자 객체만 뽑아서 DTO로 변환
        List<UserDto> participants = readStatuses.stream()
            .map(ReadStatus::getUser)
            .distinct()
            .map(userMapper::toDto)
            .collect(Collectors.toList());

        Instant lastMessageAt = messageRepository.findAllByChannel(channel).stream()
            .map(Message::getUpdatedAt)
            .max(Comparator.naturalOrder())
            .orElse(null);

        return new ChannelDto(
            channel.getId(),
            channel.getType(),
            channel.getName(),
            channel.getDescription(),
            participants,
            lastMessageAt
        );
    }
}
