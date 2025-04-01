package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.ReadStatus;
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
    public ReadStatus createReadStatus(ReadStatusCreateDto createDto) {
        checkUserExists(createDto.userId());
        checkChannelExists(createDto.channelId());
        validateReadStatusDoesNotExist(createDto.userId(), createDto.channelId());

        return readStatusRepository.save(createDto.convertCreateDtoToReadStatus());
    }

    @Override
    public ReadStatusResponseDto findById(UUID id) {
        ReadStatus readStatus = readStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 ReadStatus를 찾을 수 없습니다: " + id));
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
    public ReadStatus updateReadStatus(ReadStatusUpdateDto updateDto) {
        checkUserExists(updateDto.userId());
        checkChannelExists(updateDto.channelId());
        ReadStatus readStatus = readStatusRepository.findById(updateDto.id())
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 ReadStatus를 찾을 수 없습니다: " + updateDto.id()));

        readStatus.update(updateDto.lastReadAt());
        return readStatusRepository.save(readStatus);
    }

    @Override
    public void deleteReadStatus(UUID id) {
        checkReadStatusExists(id);
        readStatusRepository.deleteById(id);
    }


    /*******************************
     * Validation check
     *******************************/
    private void validateReadStatusDoesNotExist(UUID userId, UUID channelId) {
        if(readStatusRepository.findByUserIdAndChannelId(userId, channelId).isPresent()){
            throw new IllegalArgumentException("이미 존재하는 객체입니다. userId: " + userId + " channelId: " + channelId);
        }
    }

    private void checkReadStatusExists(UUID id) {
        if(readStatusRepository.findById(id).isEmpty()){
            throw new NoSuchElementException("해당 ReadStatus가 존재하지 않습니다. : " + id);
        }
    }

    private void checkChannelExists(UUID channelId) {
        if(channelRepository.findById(channelId).isEmpty()){
            throw new NoSuchElementException("해당 채널이 존재하지 않습니다. : " + channelId);
        }
    }

    private void checkUserExists(UUID userId) {
        if(userRepository.findById(userId).isEmpty()){
            throw new NoSuchElementException("해당 사용자가 존재하지 않습니다. : " + userId);
        }
    }

}
