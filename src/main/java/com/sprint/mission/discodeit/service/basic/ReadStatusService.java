package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.ReadStatusCreateDTO;
import com.sprint.mission.discodeit.DTO.ReadStatusUpdateDTO;
import com.sprint.mission.discodeit.Exception.CommonException;
import com.sprint.mission.discodeit.Exception.CommonExceptions;
import com.sprint.mission.discodeit.Repository.ChannelRepository;
import com.sprint.mission.discodeit.Repository.ReadStatusRepository;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReadStatusService {
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;
    private final ChannelRepository channelRepository;

    void create(ReadStatusCreateDTO readStatusCreateDTO) {
        try {
            UUID userId = UUID.fromString(readStatusCreateDTO.userId());
            UUID channelId = UUID.fromString(readStatusCreateDTO.channelID());

            User user = userRepository.find(userId);
            Channel channel = channelRepository.find(channelId);

            List<ReadStatus> list = readStatusRepository.findAllByUserId(userId);
            ReadStatus status = list.stream().filter(readStatus -> readStatus.getChannelId().equals(channelId)).findFirst().orElse(null);
            if (status == null) {
                status = new ReadStatus(userId, channelId);
            } else {
                throw CommonExceptions.DUPLICATE_READ_STATUS;
            }
            readStatusRepository.save(status);

        } catch (CommonException e) {
            System.out.println("에러가 발생하였습니다");
        }
    }

    ReadStatus find(String readStatusId) {
        UUID readStatusUUID = UUID.fromString(readStatusId);
        ReadStatus readStatus = readStatusRepository.find(readStatusUUID);
        return readStatus;
    }

    List<ReadStatus> findAllByUserId(String userId){
        UUID userUUID = UUID.fromString(userId);
        List<ReadStatus> list = readStatusRepository.findAllByUserId(userUUID);
        return list;
    }

    void update(String readStatusId, ReadStatusUpdateDTO readStatusUpdateDTO) {
        ReadStatus readStatus = find(readStatusId);
        readStatusRepository.update(readStatus, readStatusUpdateDTO);
    }

    void delete(String readStatusId){
        UUID readStatusUUID = UUID.fromString(readStatusId);
        readStatusRepository.delete(readStatusUUID);
    }
}
