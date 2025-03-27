package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusReadResponse;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.readstatus.DuplicateReadStatusException;
import com.sprint.mission.discodeit.exception.readstatus.NoSuchReadStatusException;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public UUID createReadStatus(ReadStatusCreateRequest readStatusCreateRequest) {
        UUID userId = readStatusCreateRequest.userId();
        UUID channelId = readStatusCreateRequest.channelId();

        UserService.validateUserId(userId, this.userRepository);
        ChannelService.validateChannelId(channelId, this.channelRepository);
        if (readStatusRepository.findByUserId(userId).stream()
                .anyMatch(readStatus -> readStatus.getChannelId().equals(channelId))) {
            throw new DuplicateReadStatusException("해당 userId( " + userId + ")와 channeld(" + channelId + ")를 가진 readStatus가 이미 존재합니다");
        }

        ReadStatus newReadStatus = new ReadStatus(userId, channelId);
        readStatusRepository.add(newReadStatus);
        return newReadStatus.getId();
    }

    @Override
    public ReadStatusReadResponse findReadStatusById(UUID readStatusId) {
        ReadStatus readStatus = this.readStatusRepository.findById(readStatusId);
        return new ReadStatusReadResponse(readStatus.getUserId(), readStatus.getChannelId(), readStatus.getReadTime());
    }

    @Override
    public ReadStatusReadResponse findReadStatusByUserIdChannelID(UUID userId, UUID channelId) {
        ReadStatus readStatus = this.readStatusRepository.findByUserIdChannelId(userId, channelId)
                                                        .orElseThrow(()->new NoSuchReadStatusException("해당 readStatus가 존재하지 않습니다"));
        return new ReadStatusReadResponse(readStatus.getUserId(), readStatus.getChannelId(), readStatus.getReadTime());
    }

    @Override
    public List<ReadStatusReadResponse> findAllReadStatusByUserId(UUID userId) {
        UserService.validateUserId(userId, this.userRepository);
        List<ReadStatus> readStatuses = this.readStatusRepository.findByUserId(userId);
        List<ReadStatusReadResponse> readStatusReadResponses = new ArrayList<>();
        for (ReadStatus readStatus : readStatuses) {
            readStatusReadResponses.add(new ReadStatusReadResponse(readStatus.getUserId(), readStatus.getChannelId(), readStatus.getReadTime()));
        }
        return readStatusReadResponses;
    }

    // 이 메서드는 언제 호출해야 하는가? 메세지를 작성할 때? 채널에 접속할 때? 채널에 접속해서 스크롤을 내려 메세지를 확인할 때?
    @Override
    public void updateReadStatus(ReadStatusUpdateRequest readStatusUpdateRequest) {
        ReadStatus readStatus = this.readStatusRepository.findByUserIdChannelId(readStatusUpdateRequest.userId(), readStatusUpdateRequest.channelId()).orElseThrow(()->new NoSuchReadStatusException("해당 readStatus가 존재하지 않습니다"));
        this.readStatusRepository.updateReadTime(readStatus.getId(), readStatusUpdateRequest.readTime());
    }

    @Override
    public void deleteReadStatus(UUID readStatusId) {
        this.readStatusRepository.deleteById(readStatusId);
    }
}
