package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFReadStatusRepository implements ReadStatusRepository {
    private final Map<UUID, ReadStatus> data;

    public JCFReadStatusRepository() {
        this.data = new HashMap<>();
    }

    public ReadStatus save(ReadStatusCreateRequestDto dto){
        ReadStatus readStatus = new ReadStatus(dto.getUserID(), dto.getChannelID(), dto.getLastRead());
        this.data.put(readStatus.getId(), readStatus);
        return readStatus;
    }

    public ReadStatus findById(UUID id){
        return Optional.ofNullable(data.get(id))
                .orElseThrow(() -> new NoSuchElementException("ReadStatus with id " + id + " not found"));
    }

    public List<ReadStatus> findAll(){
        return this.data.values().stream().toList();
    }

    public ReadStatus update(ReadStatusUpdateRequestDto dto){
        ReadStatus readStatus = data.get(dto.getReadStatusId());
        readStatus.update(dto.getNewLastReadAt());

        return readStatus;
    }

    public void delete(UUID readStatusID){
        data.remove(readStatusID);
    }
}
