package com.sprint.mission.discodeit.readstatus.service;

import com.sprint.mission.discodeit.channel.entity.Channel;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.readstatus.dto.ReadStatusResult;
import com.sprint.mission.discodeit.readstatus.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.readstatus.entity.ReadStatus;
import com.sprint.mission.discodeit.readstatus.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.readstatus.service.ReadStatusService;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public ReadStatusResult create(ReadStatusCreateRequest readStatusCreateRequest) {
        Channel channel = channelRepository.findById(readStatusCreateRequest.channelId())
                .orElseThrow(() -> new EntityNotFoundException("readStaus에 해당하는 채널이 없습니다."));
        User user = userRepository.findById(readStatusCreateRequest.userId())
                .orElseThrow(() -> new EntityNotFoundException("readStatus에 해당하는 유저가 없습니다."));
        if (readStatusRepository.existsByChannel_IdAndUser_Id(channel.getId(), user.getId())) {
            throw new IllegalArgumentException("해당 채널과 유저에 대한 읽기 상태가 이미 존재합니다.");
        }

        ReadStatus readStatus = readStatusRepository.save(new ReadStatus(user, channel));

        return ReadStatusResult.fromEntity(readStatus);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ReadStatusResult> getAllByUserId(UUID userId) {
        List<ReadStatus> readStatuses = readStatusRepository.findByUser_Id(userId);

        return ReadStatusResult.fromEntity(readStatuses);
    }

    @Transactional
    @Override
    public ReadStatusResult updateLastReadTime(UUID readStatusId, Instant time) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new EntityNotFoundException("해당 Id의 객체가 없습니다."));

        readStatus.updateLastReadTime(time);
        readStatusRepository.save(readStatus);

        return ReadStatusResult.fromEntity(readStatus);
    }

    @Override
    public void delete(UUID readStatusId) {
        if (!readStatusRepository.existsById(readStatusId)) {
            throw new EntityNotFoundException("해당 Id의 객체가 없습니다.");
        }

        readStatusRepository.deleteById(readStatusId);
    }

}
