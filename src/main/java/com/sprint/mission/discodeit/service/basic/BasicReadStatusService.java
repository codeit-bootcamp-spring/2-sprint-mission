package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.dto.readstatus.ReadStatusCreateParam;
import com.sprint.mission.discodeit.service.dto.readstatus.ReadStatusUpdateParam;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public void create(ReadStatusCreateParam param) {
        if (!channelRepository.existsById(param.channelId())) {
            throw new IllegalArgumentException(param.channelId() + " 에 해당하는 Channel을 찾을 수 없음");
        }
        param.userIds().forEach(userId -> {
            if (!userRepository.existsById(userId)) {
                throw new IllegalArgumentException(userId + " 에 해당하는 User을 찾을 수 없음");
            }
            readStatusRepository.findAllByUserId(userId).forEach(readStatus -> {
                if (readStatus.getChannelId().equals(param.channelId())) {
                    throw new IllegalArgumentException("중복된 객체 존재");
                }
                readStatusRepository.save(new ReadStatus(userId, param.channelId()));
            });
        });
    }

    @Override
    public ReadStatus find(UUID id) {
        return readStatusRepository.find(id)
                .orElseThrow(() -> new NoSuchElementException(id + " 에 해당하는 ReadStatusId를 찾을 수 없음"));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException(userId + " 에 해당하는 User를 찾을 수 없음.");
        }
        return readStatusRepository.findAllByUserId(userId);
    }

    @Override
    public List<UUID> findAllUserByChannelId(UUID channelId) { // channelId와 동일한 channelId에 포함된 User의 UUID 모음
        if (!channelRepository.existsById(channelId)) {
            throw new IllegalArgumentException(channelId + " 에 해당하는 Channel를 찾을 수 없음.");
        }
        return readStatusRepository.findAllUserByChannelId(channelId);
    }

    @Override
    public List<UUID> findAllByChannelId(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new IllegalArgumentException(channelId + " 에 해당하는 Channel를 찾을 수 없음.");
        }
        return readStatusRepository.findAllByChannelId(channelId);
    }

    @Override
    public void update(ReadStatusUpdateParam param) {
        ReadStatus readStatus = find(param.id());
        readStatusRepository.save(readStatus);
    }

    @Override
    public void delete(UUID id) {
        if (!readStatusRepository.existsById(id)) {
            throw new IllegalArgumentException(id + " 에 해당하는 ReadStatus을 찾을 수 없음");
        }
        readStatusRepository.delete(id);
    }
}
