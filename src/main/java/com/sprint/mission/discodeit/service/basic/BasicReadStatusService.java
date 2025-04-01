package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.handler.custom.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.handler.custom.readStatus.ReadStatusAlreadyExistsException;
import com.sprint.mission.discodeit.exception.handler.custom.readStatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.handler.custom.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.List;
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
    public ReadStatus create(ReadStatusCreateDto readStatusCreateDto) {
        boolean isExistUser = userRepository.findById(readStatusCreateDto.userId()) != null;
        if (!isExistUser) {
            throw new UserNotFoundException(readStatusCreateDto.userId() + " 유저를 찾을 수 없습니다.");
        }

        boolean isExistChannel = channelRepository.findById(readStatusCreateDto.channelId()) != null;
        if (!isExistChannel) {
            throw new ChannelNotFoundException(readStatusCreateDto.channelId() + " 채널을 찾을 수 없습니다.");
        }

        boolean isExistReadStatus = readStatusRepository.findAllByChannelId(readStatusCreateDto.channelId()).stream()
                .anyMatch(readStatus -> readStatus.getUserId().equals(readStatusCreateDto.userId()));
        if (isExistReadStatus) {
            throw new ReadStatusAlreadyExistsException(
                    readStatusCreateDto.userId() + " 유저는 이미 " + readStatusCreateDto.channelId() + " 채널을 읽었습니다.");
        }

        ReadStatus newReadStatus = new ReadStatus(readStatusCreateDto.userId(), readStatusCreateDto.channelId(),
                readStatusCreateDto.lastReadAt());

        return readStatusRepository.save(newReadStatus);
    }

    @Override
    public ReadStatus findById(UUID id) {
        ReadStatus readStatus = readStatusRepository.findById(id);

        if (readStatus == null) {
            throw new ReadStatusNotFoundException(id + " 읽은 상태를 찾을 수 없습니다.");
        }

        return readStatus;
    }

    @Override
    public List<ReadStatus> findAll() {
        return readStatusRepository.findAll();
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId);
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return readStatusRepository.findAllByChannelId(channelId);
    }

    @Override
    public ReadStatus update(ReadStatusUpdateDto readStatusUpdateDto) {
        ReadStatus readStatus = findById(readStatusUpdateDto.id());
        readStatus.update(readStatusUpdateDto.newLastReadAt());

        return readStatusRepository.save(readStatus);
    }

    @Override
    public void delete(UUID id) {
        readStatusRepository.delete(id);
    }
}
