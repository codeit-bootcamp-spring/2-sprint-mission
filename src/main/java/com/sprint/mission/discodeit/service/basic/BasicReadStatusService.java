package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.ReadStatus.ReadStatusCRUDDTO;
import com.sprint.mission.discodeit.Exception.Valid.DuplicateReadStatusException;
import com.sprint.mission.discodeit.Exception.legacy.NotFoundException;
import com.sprint.mission.discodeit.Repository.ChannelRepository;
import com.sprint.mission.discodeit.Repository.ReadStatusRepository;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.entity.ReadStatus;
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

    public UUID create(ReadStatusCRUDDTO readStatusCRUDDTO) {
        try {
            UUID userId = readStatusCRUDDTO.userId();
            UUID channelId = readStatusCRUDDTO.channelId();

            //채널, 유저가 있는지 확인하는 매커니즘
            userRepository.find(userId);
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

        } catch (NotFoundException e) {
            System.out.println("에러가 발생하였습니다");
            return null;
        }
    }

    public ReadStatus find(String readStatusId) {
        UUID readStatusUUID = UUID.fromString(readStatusId);
        ReadStatus readStatus = readStatusRepository.find(readStatusUUID);
        return readStatus;
    }

    public List<ReadStatus> findAllByUserId(String userId) {
        UUID userUUID = UUID.fromString(userId);
        List<ReadStatus> list = readStatusRepository.findAllByUserId(userUUID);
        return list;
    }

    public void update(String readStatusId, ReadStatusCRUDDTO readStatusCRUDDTO) {
        ReadStatus readStatus = find(readStatusId);
        readStatusRepository.update(readStatus, readStatusCRUDDTO);
    }

    public void delete(String readStatusId) {
        UUID readStatusUUID = UUID.fromString(readStatusId);
        readStatusRepository.delete(readStatusUUID);
    }
}
