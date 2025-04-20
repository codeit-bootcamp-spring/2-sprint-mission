package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.application.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.application.dto.readstatus.ReadStatusResult;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
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

    private final ReadStatusRepository readStatusRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    @Override
    public ReadStatusResult create(ReadStatusCreateRequest readStatusCreateRequest) {
        Channel channel = channelRepository.findById(readStatusCreateRequest.channelId())
                .orElseThrow(() -> new IllegalArgumentException("readStaus에 해당하는 채널이 없습니다."));
        User user = userRepository.findById(readStatusCreateRequest.userId())
                .orElseThrow(() -> new IllegalArgumentException("readStatus에 해당하는 유저가 없습니다."));

        readStatusRepository.findByChannelIdAndUserId(readStatusCreateRequest.channelId(),
                        readStatusCreateRequest.userId())
                .ifPresent(existingReadStatus -> {
                    throw new IllegalArgumentException("해당 채널과 유저에 대한 읽기 상태가 이미 존재합니다.");
                });

        ReadStatus readStatus = readStatusRepository.save(
                new ReadStatus(user, channel));

        return ReadStatusResult.fromEntity(readStatus);
    }

    @Override
    public List<ReadStatusResult> getAllByUserId(UUID userId) {
        List<ReadStatus> readStatuses = readStatusRepository.findByUser_Id(userId);
        return ReadStatusResult.fromEntity(readStatuses);
    }

    @Override
    public ReadStatusResult updateLastReadTime(UUID readStatusId) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new IllegalArgumentException("해당 Id의 객체가 없습니다."));

        readStatus.updateLastReadTime();
        ReadStatus savedReadStatus = readStatusRepository.save(readStatus);

        return ReadStatusResult.fromEntity(savedReadStatus);
    }

    @Override
    public void delete(UUID readStatusId) {
        readStatusRepository.deleteById(readStatusId);
    }
}
