package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.create.ReadStatusCreateRequestDTO;
import com.sprint.mission.discodeit.dto.update.ReadStatusUpdateRequestDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.NotFound.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.Valid.DuplicateReadStatusException;
import com.sprint.mission.discodeit.exception.Valid.InvalidException;
import com.sprint.mission.discodeit.logging.CustomLogging;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;
    private final ChannelRepository channelRepository;

    @CustomLogging
    @Override
    public UUID create(ReadStatusCreateRequestDTO readStatusCreateRequestDTO) {
        UUID channelId = readStatusCreateRequestDTO.channelId();
        UUID userId = readStatusCreateRequestDTO.userId();

        //채널, 유저가 있는지 체크
        checkValid(channelId, userId);

        //이미 생성된 읽기 상태가 있는지 체크
        //없으면 생성하기
        ReadStatus status = checkDuplicatedStatues(channelId, userId, readStatusCreateRequestDTO);

        readStatusRepository.save(status);

        return status.getReadStatusId();
    }

    @Override
    public ReadStatus find(UUID readStatusId) {
        return readStatusRepository.findById(readStatusId).orElseThrow(() -> new ReadStatusNotFoundException("읽기 상태를 찾을 수 없습니다."));
    }

    @Override
    public ReadStatus findByUserId(UUID userId) {
        return readStatusRepository.findByUserId(userId);
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId);
    }

    @Override
    public void update(UUID channelId, ReadStatusUpdateRequestDTO requestDTO) {
        ReadStatus readStatus = readStatusRepository.findByChannelId(channelId);
        readStatus.update(requestDTO.newLastReadAt());
    }

    @CustomLogging
    @Override
    public void delete(UUID readStatusId) {
        readStatusRepository.delete(readStatusId);
    }



    private void checkValid(UUID channelId, UUID userId) {
        if (!userRepository.existId(userId) && channelRepository.existId(channelId)) {
            throw new InvalidException("유효하지 않은 아이디입니다.");
        }
    }

    private ReadStatus checkDuplicatedStatues(UUID channelId,UUID userId, ReadStatusCreateRequestDTO readStatusCreateRequestDTO) {
        List<ReadStatus> list = readStatusRepository.findAllByUserId(userId);
        if (! list.stream().anyMatch(readStatus -> readStatus.getChannelId().equals(channelId))) {
            return new ReadStatus(userId, channelId, readStatusCreateRequestDTO.lastReadAt());
        } else {
            throw new DuplicateReadStatusException("중복된 읽기 상태 정보가 있습니다.");
        }
    }
}
