package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusReadResponse;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.common.NoSuchIdException;
import com.sprint.mission.discodeit.exception.readstatus.*;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        try {
            UUID userId = readStatusCreateRequest.userId();
            UUID channelId = readStatusCreateRequest.channelId();

            UserService.validateUserId(userId, this.userRepository);
            ChannelService.validateChannelId(channelId, this.channelRepository);
            if (readStatusRepository.findByUserId(userId).stream()
                    .anyMatch(readStatus -> readStatus.getChannelId().equals(channelId))) {
                throw new DuplicateReadStatusException("해당 userId( " + userId + ")와 channeld(" + channelId + ")를 가진 readStatus가 이미 존재합니다", HttpStatus.CONFLICT);
            }

            ReadStatus newReadStatus = new ReadStatus(userId, channelId);
            readStatusRepository.add(newReadStatus);
            return newReadStatus.getId();
        } catch (DuplicateReadStatusException e) {
            throw new CreateReadStatusException(e.getMessage(), e.getStatus(), e);
        } catch (Exception e) {
            throw new CreateReadStatusException("ReadStatus 생성 중 알 수 없는 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public ReadStatusReadResponse findReadStatusById(UUID readStatusId) {
        ReadStatus readStatus = this.readStatusRepository.findById(readStatusId);
        return new ReadStatusReadResponse(readStatus.getUserId(), readStatus.getChannelId(), readStatus.getReadTime());
    }

    @Override
    public ReadStatusReadResponse findReadStatusByUserIdChannelID(UUID userId, UUID channelId) {
        try {
            ReadStatus readStatus = this.readStatusRepository.findByUserIdChannelId(userId, channelId)
                    .orElseThrow(() -> new NoSuchReadStatusException("해당 readStatus가 존재하지 않습니다", HttpStatus.NOT_FOUND));
            return new ReadStatusReadResponse(readStatus.getUserId(), readStatus.getChannelId(), readStatus.getReadTime());
        } catch (NoSuchReadStatusException e) {
            throw new FindReadStatusException(e.getMessage(), e.getStatus(), e);
        } catch (Exception e) {
            throw new FindReadStatusException("해당 ReadStatus를 찾는 중 알 수 없는 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
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
        try {
            ReadStatus readStatus = this.readStatusRepository.findByUserIdChannelId(readStatusUpdateRequest.userId(), readStatusUpdateRequest.channelId()).orElseThrow(() -> new NoSuchReadStatusException("해당 readStatus가 존재하지 않습니다", HttpStatus.NOT_FOUND));
            this.readStatusRepository.updateReadTime(readStatus.getId(), readStatusUpdateRequest.readTime());
        } catch (NoSuchReadStatusException e) {
            throw new UpdateReadStatusException(e.getMessage(), e.getStatus(), e);
        } catch (NoSuchIdException e) {
            throw new UpdateReadStatusException(e.getMessage(), e.getStatus(), e);
        } catch (NoSuchElementException e) {
            throw new UpdateReadStatusException(e.getMessage(), HttpStatus.NOT_FOUND, e);
        } catch (Exception e) {
            throw new UpdateReadStatusException("ReadStatus 업데이트 중 알 수 없는 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public void deleteReadStatus(UUID readStatusId) {
        this.readStatusRepository.deleteById(readStatusId);
    }
}
