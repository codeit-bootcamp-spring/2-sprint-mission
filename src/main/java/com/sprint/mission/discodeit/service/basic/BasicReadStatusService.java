package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readstatus.CreateReadStatusReqDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusResDto;
import com.sprint.mission.discodeit.dto.readstatus.UpdateReadStatusReqDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public ReadStatusResDto create(CreateReadStatusReqDto createReadStatusReqDto) {
        if (!userRepository.existsById(createReadStatusReqDto.userId())) {
            throw new NoSuchElementException("해당하는 user 객체를 찾을 수 없습니다.");
        }

        if(!channelRepository.existsById(createReadStatusReqDto.channelId())) {
            throw new NoSuchElementException("해당하는 channel 객체를 찾을 수 없습니다.");
        }

        if(readStatusRepository.findAllByChannelId(createReadStatusReqDto.channelId()).stream()
                .anyMatch(readStatus -> readStatus.getUserId().equals(createReadStatusReqDto.userId()))) {
            throw new IllegalArgumentException("연관된 readStatus 객체가 이미 존재합니다.");
        }

        ReadStatus readStatus = new ReadStatus(createReadStatusReqDto.userId(), createReadStatusReqDto.channelId(), createReadStatusReqDto.lastReadAt());
        readStatusRepository.save(readStatus);
        return new ReadStatusResDto(readStatus.getId(), readStatus.getUserId(), readStatus.getChannelId(), readStatus.getLastReadAt());
    }

    @Override
    public ReadStatusResDto find(UUID readStatusId) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId).orElseThrow(() -> new NoSuchElementException("해당 하는 readStatus 객체가 없습니다."));
        return new ReadStatusResDto(readStatus.getId(), readStatus.getUserId(), readStatus.getChannelId(), readStatus.getLastReadAt());
    }

    @Override
    public List<ReadStatusResDto> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId).stream()
                .map(readStatus -> new ReadStatusResDto(readStatus.getId(), readStatus.getUserId(), readStatus.getChannelId(), readStatus.getLastReadAt())).toList();
    }

    @Override
    public ReadStatusResDto update(UUID readStatusId, UpdateReadStatusReqDto updateReadStatusReqDto) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new NoSuchElementException("해당 하는 readStatus 객체가 없습니다."));
        readStatus.updateReadStatus(updateReadStatusReqDto.lastReadAt(), Instant.now());
        ReadStatus updatedReadStatus = readStatusRepository.save(readStatus);
        return new ReadStatusResDto(updatedReadStatus.getId(), updatedReadStatus.getUserId(), updatedReadStatus.getChannelId(), updatedReadStatus.getLastReadAt());
    }

    @Override
    public void delete(UUID readStatusId) {
        if(!readStatusRepository.existsById(readStatusId)) {
            throw new NoSuchElementException("해당 하는 readStatus 객체가 없습니다.");
        }
        readStatusRepository.deleteById(readStatusId);
    }
}
