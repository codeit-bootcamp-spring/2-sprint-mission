package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

// spring bean에 자동등록해주고, (MapStruct는 DTO<->Entity 간의 변환을 자동으로 생성해줍니다.
// Channel 매핑 시 User와 관련된 것은 UserMapper에서 처리하도록 해준다.
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public abstract class ChannelMapper {

    // DB로직에 접근할 수도 있도록 repository 주입
    // 자동주입해준다.
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ReadStatusRepository readStatusRepository;
    @Autowired
    private UserMapper userMapper;

    @Mapping(target = "participants", expression = "java(resolveParticipants(channel))")
    @Mapping(target = "lastMessageAt", expression = "java(resolveLastMessageAt(channel))")
    abstract public ChannelDto toDto(Channel channel);

    protected Instant resolveLastMessageAt(Channel channel) {
        return messageRepository.findLastMessageAtByChannelId(
                channel.getId())
            .orElse(Instant.MIN);
    }

    protected List<UserDto> resolveParticipants(Channel channel) {
        List<UserDto> participants = new ArrayList<>();
        if (channel.getType().equals(ChannelType.PRIVATE)) {
            readStatusRepository.findAllByChannelIdWithUser(channel.getId())
                .stream()
                .map(ReadStatus::getUser)
                .map(userMapper::toDto)
                .forEach(participants::add);
        }
        return participants;
    }
}
