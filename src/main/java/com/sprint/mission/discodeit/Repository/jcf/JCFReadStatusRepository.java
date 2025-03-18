package com.sprint.mission.discodeit.Repository.jcf;

import com.sprint.mission.discodeit.DTO.ReadStatus.ReadStatusCRUDDTO;
import com.sprint.mission.discodeit.Exception.Empty.EmptyReadStatusListException;
import com.sprint.mission.discodeit.Exception.NotFound.ReadStatusNotFoundException;
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
    public ReadStatus save(ReadStatus readStatus) {
        readStatusList.add(readStatus);
        return readStatus;
    }

    @Override
    public ReadStatus find(UUID readStatusId) {
        ReadStatus status = CommonUtils.findById(readStatusList, readStatusId, ReadStatus::getReadStatusId)
                .orElseThrow(() -> new ReadStatusNotFoundException("읽기 상태를 찾을 수 없습니다."));
        return status;
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userID) {
        if (readStatusList.isEmpty()) {
            throw new EmptyReadStatusListException("Repository 에 저장된 읽기 상태 리스트가 없습니다.");
        }
        List<ReadStatus> list = CommonUtils.findAllById(readStatusList, userID, ReadStatus::getUserId);

        if (list.isEmpty()) {
            throw new EmptyReadStatusListException("해당 서버에 저장된 읽기 상태 리스트가 없습니다.");
        }
        return list;
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        if (readStatusList.isEmpty()) {
            throw new EmptyReadStatusListException("Repository 에 저장된 읽기 상태 리스트가 없습니다.");
        }

        List<ReadStatus> list = CommonUtils.findAllById(readStatusList, channelId, ReadStatus::getChannelId);

        if (list.isEmpty()) {
            throw new EmptyReadStatusListException("해당 서버에 저장된 읽기 상태 리스트가 없습니다.");
        }
        return list;
    }

    @Override
    public ReadStatus update(ReadStatus readStatus, ReadStatusCRUDDTO readStatusCRUDDTO) {
        if (readStatusCRUDDTO.readStatusId() != null) {
            readStatus.setReadStatusId(readStatusCRUDDTO.readStatusId());
        }
        return readStatus;
    }

    @Override
    public void delete(UUID readStatusId) {
        ReadStatus status = find(readStatusId);
        readStatusList.remove(status);
    }
}
