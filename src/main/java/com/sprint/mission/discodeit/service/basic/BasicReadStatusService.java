package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.readStatus.CreateReadStatusParam;
import com.sprint.mission.discodeit.dto.service.readStatus.ReadStatusDTO;
import com.sprint.mission.discodeit.dto.service.readStatus.UpdateReadStatusDTO;
import com.sprint.mission.discodeit.dto.service.readStatus.UpdateReadStatusParam;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.RestException;
import com.sprint.mission.discodeit.exception.RestExceptions;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
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
    private final ReadStatusMapper readStatusMapper;

    @Override
    public ReadStatusDTO create(CreateReadStatusParam createReadStatusParam) {
        checkUserExists(createReadStatusParam);
        checkChannelExists(createReadStatusParam);
        checkDuplicateReadStatus(createReadStatusParam);

        ReadStatus readStatus = readStatusMapper.toEntity(createReadStatusParam);
        readStatusRepository.save(readStatus);
        return readStatusMapper.toReadStatusDTO(readStatus);
    }

    @Override
    public ReadStatusDTO find(UUID id) {
        ReadStatus readStatus = findReadStatusById(id);
        return readStatusMapper.toReadStatusDTO(readStatus);
    }

    @Override
    public List<ReadStatusDTO> findAllByUserId(UUID userId) {
        List<ReadStatus> readStatuses = readStatusRepository.findAllByUserId(userId);
        return readStatuses.stream()
                .map(rs -> readStatusMapper.toReadStatusDTO(rs))
                .collect(Collectors.toList());
    }

    @Override
    public List<ReadStatusDTO> findAllByChannelId(UUID channelId) {
        List<ReadStatus> readStatuses = readStatusRepository.findAllByChannelId(channelId);
        return readStatuses.stream()
                .map(rs -> readStatusMapper.toReadStatusDTO(rs))
                .collect(Collectors.toList());
    }

    @Override
    public UpdateReadStatusDTO update(UpdateReadStatusParam updateReadStatusParam) {
        ReadStatus readStatus = findReadStatusById(updateReadStatusParam.id());
        readStatus.updateReadStatus();
        readStatusRepository.save(readStatus);
        return new UpdateReadStatusDTO(updateReadStatusParam.id(), readStatus.getUpdatedAt());
    }

    @Override
    public void delete(UUID id) {
        // remove의 경우 id가 없는 경우에는 예외를 던지지 않고 그냥 무시됨
        // 굳이 find해서 있는지 확인하고 지울 필요 없다!
        readStatusRepository.deleteById(id);
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        readStatusRepository.deleteByChannelId(channelId);
    }

    private void checkUserExists(CreateReadStatusParam createReadStatusParam) {
        userRepository.findById(createReadStatusParam.userId())
                .orElseThrow(() -> RestExceptions.USER_NOT_FOUND);
    }

    private void checkChannelExists(CreateReadStatusParam createReadStatusParam) {
        channelRepository.findById(createReadStatusParam.channelId())
                .orElseThrow(() -> RestExceptions.CHANNEL_NOT_FOUND);
    }

    private void checkDuplicateReadStatus(CreateReadStatusParam createReadStatusParam) {
        if (readStatusRepository.existsByUserIdAndChannelId(createReadStatusParam.userId(), createReadStatusParam.channelId())) {
            throw RestExceptions.DUPLICATE_READ_STATUS;
        }
    }

    private ReadStatus findReadStatusById(UUID id) {
        return readStatusRepository.findById(id)
                .orElseThrow(() -> RestExceptions.READ_STATUS_NOT_FOUND);
    }
}
