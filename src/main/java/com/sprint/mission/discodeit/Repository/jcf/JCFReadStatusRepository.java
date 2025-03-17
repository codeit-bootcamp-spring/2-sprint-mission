package com.sprint.mission.discodeit.Repository.jcf;

import com.sprint.mission.discodeit.DTO.ReadStatusUpdateDTO;
import com.sprint.mission.discodeit.Exception.CommonExceptions;
import com.sprint.mission.discodeit.Repository.ReadStatusRepository;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFReadStatusRepository implements ReadStatusRepository {
    private final List<ReadStatus> readStatusList = new ArrayList<>();

    @Override
    public void save(ReadStatus readStatus) {
        readStatusList.add(readStatus);
    }

    @Override
    public ReadStatus find(UUID readStatusId) {
        ReadStatus status = readStatusList.stream().filter(readStatus -> readStatus.getReadStatusId().equals(readStatusId))
                .findFirst().orElseThrow(() -> CommonExceptions.READ_STATUS_NOT_FOUND);
        return status;
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userID) {
        if (readStatusList.isEmpty()) {
            throw CommonExceptions.EMPTY_READ_STATUS_LIST;
        }

        List<ReadStatus> list = readStatusList.stream().filter(readStatus -> readStatus.getUserId().equals(userID))
                .toList();

        if (list.isEmpty()) {
            throw CommonExceptions.EMPTY_READ_STATUS_LIST;
        }
        return list;
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        if (readStatusList.isEmpty()) {
            throw CommonExceptions.EMPTY_READ_STATUS_LIST;
        }
        List<ReadStatus> list = readStatusList.stream().filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .toList();

        if (list.isEmpty()) {
            throw CommonExceptions.EMPTY_READ_STATUS_LIST;
        }
        return list;
    }

    @Override
    public void update(ReadStatus readStatus,ReadStatusUpdateDTO readStatusUpdateDTO) {
        if (readStatusUpdateDTO.replaceId() != null) {
            readStatus.setReadStatusId(readStatusUpdateDTO.replaceId());
        }
    }

    @Override
    public void delete(UUID readStatusId) {
        ReadStatus status = find(readStatusId);
        readStatusList.remove(status);
    }
}
