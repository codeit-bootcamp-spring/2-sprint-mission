package com.sprint.mission.discodeit.Repository.jcf;

import com.sprint.mission.discodeit.DTO.ReadStatusUpdateDTO;
import com.sprint.mission.discodeit.Exception.ReadStatusNotFoundException;
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
                .findFirst().orElseThrow(() -> new ReadStatusNotFoundException("해당 ID를 가지는 read Status 를 찾을 수 없습니다."));
        return status;
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userID) {
        List<ReadStatus> list = readStatusList.stream().filter(readStatus -> readStatus.getUserId().equals(userID))
                .toList();
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
