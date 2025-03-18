package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusFindResponse;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdatetRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public UUID createReadStatus(ReadStatusCreateRequest readStatusCreateRequest) {
        if (!userRepository.existsById(readStatusCreateRequest.userId())) {
            throw new NoSuchElementException("해당 id를 가진 user가 존재하지 않습니다 : " + readStatusCreateRequest.userId());
        }
        if (!channelRepository.existsById(readStatusCreateRequest.channelId())) {
            throw new NoSuchElementException("해당 id를 가진 channel이 존재하지 않습니다 : " + readStatusCreateRequest.channelId());
        }
        ReadStatus newReadStatus = new ReadStatus(readStatusCreateRequest.userId(), readStatusCreateRequest.channelId());
        readStatusRepository.add(newReadStatus);
        return newReadStatus.getId();
    }

    @Override
    public ReadStatusFindResponse findReadStatus(UUID readStatusId) {
        ReadStatus readStatus = this.readStatusRepository.findById(readStatusId);
        return new ReadStatusFindResponse(readStatus.getId());
    }

    @Override
    public List<ReadStatusFindResponse> findAllReadStatusByUserId(UUID userId) {
        List<ReadStatus> readStatuses = this.readStatusRepository.findByUserId(userId);
        List<ReadStatusFindResponse> readStatusFindResponses = new ArrayList<>();
        for (ReadStatus readStatus : readStatuses) {
            readStatusFindResponses.add(new ReadStatusFindResponse(readStatus.getId()));
        }
        return readStatusFindResponses;
    }

    @Override
    public void updateReadStatus(ReadStatusUpdatetRequest readStatusUpdateRequest) {

    }

    @Override
    public void deleteReadStatus(UUID readStatusId) {

    }
}
