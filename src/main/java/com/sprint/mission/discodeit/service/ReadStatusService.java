package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ReadStatusCreateDto;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    public ReadStatusService(ReadStatusRepository readStatusRepository, UserRepository userRepository, ChannelRepository channelRepository) {
        this.readStatusRepository = readStatusRepository;
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
    }

    public void createReadStatus(ReadStatusCreateDto dto) {
        if (!channelRepository.existsById(dto.getChannelId())) {
            throw new IllegalArgumentException("Channel " + dto.getChannelId() + "이 존재하지 않습니다.");
        }
        if (!userRepository.existsById(dto.getUserId())) {
            throw new IllegalArgumentException("User " + dto.getUserId() + "이 존재하지 않습니다.");
        }

        ReadStatus existingReadStatus = readStatusRepository.findReadStatusById(dto.getChannelId());
        if (existingReadStatus != null && existingReadStatus.getUserIds().containsKey(dto.getUserId())) {
            throw new IllegalStateException("이미 존재하는 ReadStatus입니다.");
        }

        ReadStatus readStatus = existingReadStatus != null ? existingReadStatus : new ReadStatus(dto.getChannelId());
        readStatus.addUser(dto.getUserId());
        readStatusRepository.addReadStatus(readStatus);
    }

    public ReadStatus findReadStatusById(UUID id) {
        ReadStatus readStatus = readStatusRepository.findReadStatusById(id);
        if (readStatus == null) {
            throw new IllegalArgumentException("ReadStatus " + id + "을 찾을 수 없습니다.");
        }
        return readStatus;
    }

    public List<ReadStatus> findAll() {
        return readStatusRepository.findAllReadStatus();
    }

    public void updateReadStatus(ReadStatusUpdateDto dto) {
        ReadStatus readStatus = readStatusRepository.findReadStatusById(dto.getId());
        if (readStatus == null) {
            throw new IllegalArgumentException("ReadStatus " + dto.getId() + "을 찾을 수 없습니다.");
        }

        if (dto.getUserId() != null) {
            readStatus.updateLastAccessTime(dto.getUserId());
        }

        readStatusRepository.addReadStatus(readStatus);
    }

    public void updateByUserId(ReadStatusUpdateDto dto) {
        readStatusRepository.updateTime(dto.getChannelId(), dto.getUserId());
    }

    public void deleteReadStatus(UUID id) {
        if (!readStatusRepository.existReadStatusById(id)) {
            throw new IllegalArgumentException("ReadStatus " + id + "을 찾을 수 없습니다.");
        }
        readStatusRepository.deleteReadStatusById(id);
    }
}
