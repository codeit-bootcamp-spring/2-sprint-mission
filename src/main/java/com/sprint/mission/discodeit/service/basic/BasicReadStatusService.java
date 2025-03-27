package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public ReadStatus create(ReadStatusCreateRequestDto dto) {
        User user = Optional.ofNullable(userRepository.findById(dto.getUserID()))
                .orElseThrow(() -> new NoSuchElementException("User with id " + dto.getUserID() + " not found"));

        Channel channel = Optional.ofNullable(channelRepository.findById(dto.getChannelID()))
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + dto.getChannelID() + " not found"));

        if(readStatusRepository.findAll().stream()
                .anyMatch(readStatus ->
                        (readStatus.getUserId().equals(user.getId()))&&(readStatus.getChannelId().equals(channel.getId())))) {
            throw new IllegalArgumentException("관련된 객체가 이미 존재합니다.");
        }

        ReadStatus readStatus = new ReadStatus(dto.getUserID(), dto.getChannelID(), dto.getLastRead());

        return readStatusRepository.save(readStatus);
    }

    @Override
    public ReadStatus findById(UUID id) {
        return Optional.ofNullable(readStatusRepository.findById(id))
                .orElseThrow(() -> new NoSuchElementException("ReadStatus with id " + id + " not found"));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAll().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .toList();
    }

    @Override
    public ReadStatus update(ReadStatusUpdateRequestDto dto){
        ReadStatus readStatus = readStatusRepository.findById(dto.getReadStatusId());
        if (readStatus == null) {
            throw new NoSuchElementException("UserStatus with id " + dto.getReadStatusId() + " not found");
        }

        return readStatusRepository.update(dto);
    }

    @Override
    public void delete(UUID readStatusId){
        readStatusRepository.delete(readStatusId);
    }
}
