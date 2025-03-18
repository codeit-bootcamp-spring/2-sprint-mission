package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusFindResponse;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserService;
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
        UserService.validateUserId(readStatusCreateRequest.userId(), this.userRepository);
        ChannelService.validateChannelId(readStatusCreateRequest.channelId(), this.channelRepository);
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
        UserService.validateUserId(userId, this.userRepository);
        List<ReadStatus> readStatuses = this.readStatusRepository.findByUserId(userId);
        List<ReadStatusFindResponse> readStatusFindResponses = new ArrayList<>();
        for (ReadStatus readStatus : readStatuses) {
            readStatusFindResponses.add(new ReadStatusFindResponse(readStatus.getId()));
        }
        return readStatusFindResponses;
    }

    @Override
    public void updateReadStatus(ReadStatusUpdateRequest readStatusUpdateRequest) {
        this.readStatusRepository.updateReadTime(readStatusUpdateRequest.id(), readStatusUpdateRequest.readTime());
    }

    @Override
    public void deleteReadStatus(UUID readStatusId) {
        this.readStatusRepository.deleteById(readStatusId);
    }
}
