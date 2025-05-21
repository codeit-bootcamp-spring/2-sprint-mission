package com.sprint.mission.discodeit.domain.channel.mapper;

import com.sprint.mission.discodeit.domain.channel.dto.service.ChannelResult;
import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.message.repository.MessageRepository;
import com.sprint.mission.discodeit.domain.readstatus.entity.ReadStatus;
import com.sprint.mission.discodeit.domain.readstatus.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.domain.user.dto.UserResult;
import com.sprint.mission.discodeit.domain.user.mapper.UserResultMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ChannelMapper {

    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final UserResultMapper userResultMapper;

    public ChannelResult convertToChannelResult(Channel channel) {
        Instant lastMessageCreatedAt = messageRepository.findLastMessageCreatedAtByChannelId(channel.getId())
                .orElse(null);
        if (channel.isPrivate()) {
            List<UserResult> participants = readStatusRepository.findByChannelId(channel.getId())
                    .stream()
                    .map(ReadStatus::getUser)
                    .map(userResultMapper::convertToUserResult)
                    .toList();

            return ChannelResult.fromPrivate(channel, lastMessageCreatedAt, participants);
        }

        return ChannelResult.fromPublic(channel, lastMessageCreatedAt);
    }

}
