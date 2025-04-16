package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exceptions.InvalidInputException;
import com.sprint.mission.discodeit.exceptions.NotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.dto.readstatusdto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusJPARepository readStatusJpaRepository;
    private final UserJPARepository userJpaRepository;
    private final ChannelJPARepository channelJpaRepository;
    private final ReadStatusMapper readStatusMapper;

    @Override
    @Transactional
    public ReadStatusResponseDto create(ReadStatusCreateDto readStatusCreateDto) {
        User matchingUser = userJpaRepository.findById(readStatusCreateDto.userId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Channel matchingChannel = channelJpaRepository.findById(readStatusCreateDto.channelId())
                .orElseThrow(() -> new NotFoundException("Channel not found"));

        if(readStatusJpaRepository.existsByUser_IdAndChannel_Id(matchingUser.getId(), matchingChannel.getId())) {
            throw new InvalidInputException("Read status already exists");
        }

        Instant lastReadAt = readStatusCreateDto.lastReadAt();
        ReadStatus readStatus = new ReadStatus(matchingUser, matchingChannel, lastReadAt);
        readStatusJpaRepository.save(readStatus);

        return readStatusMapper.toDto(readStatus);

    }


    @Override
    public ReadStatusResponseDto find(ReadStatusFindDto readStatusFindDto) {
        ReadStatus readStatus = readStatusJpaRepository.findById(readStatusFindDto.Id())
                .orElseThrow(() -> new NotFoundException("readStatus not found"));

        return readStatusMapper.toDto(readStatus);
    }


    @Override
    public List<ReadStatusResponseDto> findAllByUserId(UUID userId) {
        List<ReadStatusResponseDto> readStatusAllList = new ArrayList<>();
        readStatusJpaRepository.findByUser_Id(userId).stream()
                .map(readStatusMapper::toDto)
                .forEach(readStatusAllList::add);
        return readStatusAllList;
    }


    @Override
    @Transactional
    public ReadStatusResponseDto update(UUID readStatusId, ReadStatusUpdateDto readStatusUpdateDto) {
        ReadStatus matchingReadStatus = readStatusJpaRepository.findById(readStatusId)
                .orElseThrow(() -> new NotFoundException("readStatus not found"));

        matchingReadStatus.readStatusUpdate(readStatusUpdateDto.newLastReadAt());
        readStatusJpaRepository.save(matchingReadStatus);
        return readStatusMapper.toDto(matchingReadStatus);
    }


    @Override
    @Transactional
    public void delete(ReadStatusDeleteDto readStatusDeleteDto) {
        ReadStatus matchingReadStatus = readStatusJpaRepository.findById(readStatusDeleteDto.Id())
                .orElseThrow(() -> new NotFoundException("readStatus not found"));
        readStatusJpaRepository.delete(matchingReadStatus);
    }
}
