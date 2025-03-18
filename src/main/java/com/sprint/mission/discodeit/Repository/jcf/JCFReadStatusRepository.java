package com.sprint.mission.discodeit.Repository.jcf;

import com.sprint.mission.discodeit.DTO.ReadStatus.ReadStatusCRUDDTO;
import com.sprint.mission.discodeit.Exception.NotFoundExceptions;
import com.sprint.mission.discodeit.Exception.EmptyExceptions;
import com.sprint.mission.discodeit.Repository.ReadStatusRepository;
import com.sprint.mission.discodeit.Util.CommonUtils;
import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFReadStatusRepository implements ReadStatusRepository {
    private final List<ReadStatus> readStatusList = new ArrayList<>();

    @Override
    public void save(ReadStatus readStatus) {
        readStatusList.add(readStatus);
    }

    @Override
    public ReadStatus find(UUID readStatusId) {
        ReadStatus status = CommonUtils.findById(readStatusList, readStatusId, ReadStatus::getReadStatusId)
                .orElseThrow(() -> NotFoundExceptions.READ_STATUS_NOT_FOUND);
        return status;
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userID) {
        if (readStatusList.isEmpty()) {
            throw EmptyExceptions.EMPTY_READ_STATUS_LIST;
        }
        List<ReadStatus> list = CommonUtils.findAllById(readStatusList, userID, ReadStatus::getUserId);

        if (list.isEmpty()) {
            throw EmptyExceptions.EMPTY_READ_STATUS_LIST;
        }
        return list;
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        if (readStatusList.isEmpty()) {
            throw EmptyExceptions.EMPTY_READ_STATUS_LIST;
        }

        List<ReadStatus> list = CommonUtils.findAllById(readStatusList, channelId, ReadStatus::getChannelId);

        if (list.isEmpty()) {
            throw EmptyExceptions.EMPTY_READ_STATUS_LIST;
        }
        return list;
    }

    @Override
    public void update(ReadStatus readStatus, ReadStatusCRUDDTO readStatusCRUDDTO) {
        if (readStatusCRUDDTO.readStatusId() != null) {
            readStatus.setReadStatusId(readStatusCRUDDTO.readStatusId());
        }
    }

    @Override
    public void delete(UUID readStatusId) {
        ReadStatus status = find(readStatusId);
        readStatusList.remove(status);
    }
}
