package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.readStatus.CreateReadStatusParam;
import com.sprint.mission.discodeit.dto.service.readStatus.ReadStatusDTO;
import com.sprint.mission.discodeit.dto.service.readStatus.UpdateReadStatusParam;
import com.sprint.mission.discodeit.entity.ReadStatus;
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

    @Override
    public ReadStatusDTO create(CreateReadStatusParam createReadStatusParam) {
        checkUserExists(createReadStatusParam);
        checkChannelExists(createReadStatusParam);
        checkDuplicateReadStatus(createReadStatusParam);

        ReadStatus readStatus = createReadStatusEntity(createReadStatusParam);
        readStatusRepository.save(readStatus);
        return readStatusEntityToDTO(readStatus);
    }

    @Override
    public ReadStatusDTO find(UUID id) {
        ReadStatus readStatus = findReadStatusById(id);
        return readStatusEntityToDTO(readStatus);
    }

    @Override
    public List<ReadStatusDTO> findAllByUserId(UUID userId) {
        List<ReadStatus> readStatuses = readStatusRepository.findAllByUserId(userId);
        return readStatuses.stream()
                .map(rs -> readStatusEntityToDTO(rs))
                .collect(Collectors.toList());
    }

    @Override
    public List<ReadStatusDTO> findAllByChannelId(UUID channelId) {
        List<ReadStatus> readStatuses = readStatusRepository.findAllByChannelId(channelId);
        return readStatuses.stream()
                .map(rs -> readStatusEntityToDTO(rs))
                .collect(Collectors.toList());
    }

    @Override
    public UUID update(UpdateReadStatusParam updateReadStatusParam) {
        ReadStatus readStatus = findReadStatusById(updateReadStatusParam.id());
        readStatus.updateReadStatus();
        readStatusRepository.save(readStatus);
        return readStatus.getId();
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
                .orElseThrow(() -> new NoSuchElementException(createReadStatusParam.userId() + "user가 존재하지 않습니다."));
    }

    private void checkChannelExists(CreateReadStatusParam createReadStatusParam) {
        channelRepository.findById(createReadStatusParam.channelId())
                .orElseThrow(() -> new NoSuchElementException(createReadStatusParam.channelId() + "channel이 존재하지 않습니다"));
    }

    private void checkDuplicateReadStatus(CreateReadStatusParam createReadStatusParam) {
        if (readStatusRepository.existsByUserIdAndChannelId(createReadStatusParam.userId(), createReadStatusParam.channelId())) {
            throw new IllegalStateException("이미 ReadStatus가 존재하는 userId와 channelId 입니다.");
        }
    }

    private ReadStatus findReadStatusById(UUID id) {
        return readStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(id + " ReadStatus가 존재하지 않습니다."));
    }

    private ReadStatus createReadStatusEntity(CreateReadStatusParam createReadStatusParam) {
        return ReadStatus.builder()
                .userId(createReadStatusParam.userId())
                .channelId(createReadStatusParam.channelId())
                .build();
    }

    private ReadStatusDTO readStatusEntityToDTO(ReadStatus readStatus) {
        return ReadStatusDTO.builder()
                .id(readStatus.getId())
                .channelId(readStatus.getChannelId())
                .userId(readStatus.getUserId())
                .build();
    }


}
