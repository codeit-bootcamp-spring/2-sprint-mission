package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.dto.ReadStatusCreateDto;
import com.sprint.mission.discodeit.service.dto.ReadStatusResponseDto;
import com.sprint.mission.discodeit.service.dto.ReadStatusUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public UUID createReadStatus(ReadStatusCreateDto createDto) {
        checkUserExists(createDto.userId());
        checkChannelExists(createDto.channelId());

        return readStatusRepository.createReadStatus(createDto.convertCreateDtoToReadStatus());
    }

    @Override
    public ReadStatusResponseDto findById(UUID id) {
        ReadStatus readStatus = readStatusRepository.findById(id);
        return ReadStatusResponseDto.convertToResponseDto(readStatus);
    }

    @Override
    public List<ReadStatusResponseDto> findAllByUserId(UUID userId) {
        List<ReadStatus> readStatusList = readStatusRepository.findAllByUserId(userId);
        return readStatusList.stream()
                .map(ReadStatusResponseDto::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public void updateReadStatus(ReadStatusUpdateDto updateDto) {
        checkUserExists(updateDto.userId());
        checkChannelExists(updateDto.channelId());

        readStatusRepository.updateReadStatus(updateDto.id(), updateDto.lastReadAt());
    }

    @Override
    public void deleteReadStatus(UUID id) {
        ReadStatus readStatus = readStatusRepository.findById(id);
        checkUserExists(readStatus.getUserId());
        checkChannelExists(readStatus.getChannelId());

        readStatusRepository.deleteReadStatus(id);
    }

    /*******************************
     * Validation check
     *******************************/
    private void checkChannelExists(UUID channelId) {
        if(channelRepository.findById(channelId) == null){
            throw new NoSuchElementException("해당 채널이 존재하지 않습니다. : " + channelId);
        }
    }

    private void checkUserExists(UUID userId) {
        if(userRepository.findById(userId) == null){
            throw new NoSuchElementException("해당 사용자가 존재하지 않습니다. : " + userId);
        }
    }

}
