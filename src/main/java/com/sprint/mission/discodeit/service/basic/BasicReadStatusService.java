package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.CreateReadStatusRequestDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.Valid.DuplicateReadStatusException;
import com.sprint.mission.discodeit.logging.CustomLogging;
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
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;
    private final ChannelRepository channelRepository;

    @CustomLogging
    @Override
    public UUID create(CreateReadStatusRequestDTO createReadStatusRequestDTO) {
            UUID userId = createReadStatusRequestDTO.userId();
            UUID channelId = createReadStatusRequestDTO.channelId();

            //채널, 유저가 있는지 확인하는 매커니즘
            userRepository.findById(userId);
            channelRepository.find(channelId);

            List<ReadStatus> list = readStatusRepository.findAllByUserId(userId);
            ReadStatus status = list.stream().filter(readStatus -> readStatus.getChannelId().equals(channelId)).findFirst().orElse(null);

            if (status == null) {
                status = new ReadStatus(userId, channelId);
            } else {
                throw new DuplicateReadStatusException("중복된 읽기 상태 정보가 있습니다.");
            }
            readStatusRepository.save(status);

            return status.getReadStatusId();


    }
    @Override
    public ReadStatus find(UUID readStatusId) {
        ReadStatus readStatus = readStatusRepository.find(readStatusId);
        return readStatus;
    }
    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        List<ReadStatus> list = readStatusRepository.findAllByUserId(userId);
        return list;
    }

//    @Override
//    public void update(UUID readStatusId) {
//        ReadStatus readStatus = find(readStatusId);
//        readStatusRepository.update(readStatus);
//    }

    @CustomLogging
    @Override
    public void delete(UUID readStatusId) {
        readStatusRepository.delete(readStatusId);
    }
}
