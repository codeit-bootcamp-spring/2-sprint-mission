package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exceptions.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exceptions.readstatus.DuplicateReadStatusException;
import com.sprint.mission.discodeit.exceptions.readstatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exceptions.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.mapper.ResponseMapStruct;
import com.sprint.mission.discodeit.repository.ChannelJPARepository;
import com.sprint.mission.discodeit.repository.ReadStatusJPARepository;
import com.sprint.mission.discodeit.repository.UserJPARepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.dto.request.readstatusdto.ReadStatusCreateDto;
import com.sprint.mission.discodeit.service.dto.request.readstatusdto.ReadStatusDeleteDto;
import com.sprint.mission.discodeit.service.dto.request.readstatusdto.ReadStatusFindDto;
import com.sprint.mission.discodeit.service.dto.request.readstatusdto.ReadStatusUpdateDto;
import com.sprint.mission.discodeit.service.dto.response.ReadStatusResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusJPARepository readStatusJpaRepository;
    private final UserJPARepository userJpaRepository;
    private final ChannelJPARepository channelJpaRepository;
    private final ReadStatusMapper readStatusMapper;
    private final ResponseMapStruct responseMapStruct;

    @Override
    @Transactional
    public ReadStatusResponseDto create(ReadStatusCreateDto readStatusCreateDto) {
        User matchingUser = userJpaRepository.findById(readStatusCreateDto.userId())
                .orElseThrow(() -> new UserNotFoundException(Map.of("userId", readStatusCreateDto.userId())));

        Channel matchingChannel = channelJpaRepository.findById(readStatusCreateDto.channelId())
                .orElseThrow(() -> new ChannelNotFoundException(Map.of("channelId", readStatusCreateDto.channelId())));

        if (readStatusJpaRepository.existsByUser_IdAndChannel_Id(matchingUser.getId(), matchingChannel.getId())) {
            throw new DuplicateReadStatusException(Map.of("userId", matchingUser.getId(), "channelId", matchingChannel.getId()));
        }

        Instant lastReadAt = readStatusCreateDto.lastReadAt();
        ReadStatus readStatus = new ReadStatus(matchingUser, matchingChannel, lastReadAt);
        readStatusJpaRepository.save(readStatus);

        return responseMapStruct.toReadStatusDto(readStatus);

    }


    @Override
    @Transactional(readOnly = true)
    public ReadStatusResponseDto find(ReadStatusFindDto readStatusFindDto) {
        ReadStatus readStatus = readStatusJpaRepository.findById(readStatusFindDto.Id())
                .orElseThrow(() -> new ReadStatusNotFoundException(Map.of("readStatusId", readStatusFindDto.Id())));

        return responseMapStruct.toReadStatusDto(readStatus);
    }


    @Override
    @Transactional(readOnly = true)
    public List<ReadStatusResponseDto> findAllByUserId(UUID userId) {
        List<ReadStatusResponseDto> readStatusAllList = new ArrayList<>();
        readStatusJpaRepository.findByUser_Id(userId).stream()
                .map(responseMapStruct::toReadStatusDto)
                .forEach(readStatusAllList::add);
        return readStatusAllList;
    }


    @Override
    @Transactional
    public ReadStatusResponseDto update(UUID readStatusId, ReadStatusUpdateDto readStatusUpdateDto) {
        ReadStatus matchingReadStatus = readStatusJpaRepository.findById(readStatusId)
                .orElseThrow(() -> new ReadStatusNotFoundException(Map.of("readStatusId", readStatusId)));

        matchingReadStatus.readStatusUpdate(readStatusUpdateDto.newLastReadAt());
        readStatusJpaRepository.save(matchingReadStatus);
        return responseMapStruct.toReadStatusDto(matchingReadStatus);
    }


    @Override
    @Transactional
    public void delete(ReadStatusDeleteDto readStatusDeleteDto) {
        ReadStatus matchingReadStatus = readStatusJpaRepository.findById(readStatusDeleteDto.Id())
                .orElseThrow(() -> new ReadStatusNotFoundException(Map.of("readStatusId", readStatusDeleteDto.Id())));
        readStatusJpaRepository.delete(matchingReadStatus);
    }
}
