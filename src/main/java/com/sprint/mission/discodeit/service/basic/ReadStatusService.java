package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusDto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.service.ReadStatusDto.ReadStatusUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    public ReadStatus createReadStatus(ReadStatusCreateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Channel channel = channelRepository.findById(request.channelId())
                .orElseThrow(() -> new RuntimeException("Channel not found"));

        if (readStatusRepository.existsByUserIdAndChannelId(request.userId(), request.channelId())) {
            throw new IllegalStateException("ReadStatus already exists for this user and channel");
        }

        ReadStatus readStatus = new ReadStatus(request.userId(), request.channelId());
        return readStatusRepository.save(readStatus);
    }

    public ReadStatus findById(UUID id) {
        return readStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ReadStatus not found"));
    }

    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId);
    }

    public void updateReadStatus(ReadStatusUpdateRequest request) {
        ReadStatus readStatus = readStatusRepository.findById(request.readStatusId())
                .orElseThrow(() -> new RuntimeException("ReadStatus not found"));

        readStatus.update(request);
        readStatusRepository.save(readStatus);
    }

    public void deleteReadStatus(UUID id) {
        readStatusRepository.delete(id);
    }
}
