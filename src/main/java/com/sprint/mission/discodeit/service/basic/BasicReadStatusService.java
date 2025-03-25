package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public void create(ReadStatusDto readStatusCreateDto) {
        User user = userRepository.findById(readStatusCreateDto.userId()).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        Channel channel = channelRepository.findById(readStatusCreateDto.channelId()).orElseThrow(() -> new IllegalArgumentException("해당 채널이 존재하지 않습니다."));

        if(readStatusRepository.existsByUserIdAndChannelId(user.getId(), channel.getId())){
            throw new IllegalStateException("이미 존재하는 Channel과 User관계입니다.");
        }

        readStatusRepository.save(new ReadStatus(readStatusCreateDto.id(), readStatusCreateDto.userId(), readStatusCreateDto.channelId(), Boolean.FALSE));
    }

    @Override
    public ReadStatusDto findById(UUID id) {
        return readStatusRepository
                .findById(id)
                .map(ReadStatusDto::from)
                .orElseThrow( () -> new IllegalArgumentException("찾을 수없는 아이디입니다."));
    }

    @Override
    public List<ReadStatusDto> findAllByUserId(UUID userId) {
        return readStatusRepository
                .findAllByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다"))
                .stream()
                .map(ReadStatusDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public void update(ReadStatusUpdateDto readStatusUpdateDto) {
        readStatusRepository.update(new ReadStatus(readStatusUpdateDto.id(), readStatusUpdateDto.userId(), readStatusUpdateDto.channelId(), readStatusUpdateDto.readStatus()));
    }

    @Override
    public void delete(UUID id) {
        readStatusRepository.delete(id);
    }
}
