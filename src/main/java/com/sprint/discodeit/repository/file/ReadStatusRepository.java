package com.sprint.discodeit.repository.file;

import com.sprint.discodeit.domain.entity.ReadStatus;
import com.sprint.discodeit.repository.util.AbstractFileRepository;
import com.sprint.discodeit.repository.util.FilePathUtil;
import com.sprint.discodeit.repository.util.SaveRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class ReadStatusRepository extends AbstractFileRepository<ReadStatus> implements SaveRepository<ReadStatus> {

    protected ReadStatusRepository() {
        super(FilePathUtil.READSTATUS.getPath());
    }

    @Override
    public void save(ReadStatus readStatus) {
        Map<UUID, ReadStatus> readStatusMap = loadAll();
        if (readStatusMap.containsKey(readStatus.getId())) {
            throw new IllegalArgumentException("[DEBUG] 동일한 UUID의 데이터가 이미 존재하므로 추가하지 않음: " + readStatus.getId());
        } else {
            readStatusMap.put(readStatus.getId(), readStatus);
            writeToFile(readStatusMap);
        }
    }

    @Override
    public void delete(UUID channelId) {
        Map<UUID, ReadStatus> readStatusMap = loadAll();

        readStatusMap.values().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .findFirst()
                .ifPresent(readStatus -> {
                    readStatusMap.remove(readStatus.getId());
                    writeToFile(readStatusMap);
                });
    }


    public Optional<ReadStatus> findById(UUID uuid) {
        Map<UUID, ReadStatus> readStatusMap = loadAll();
        return Optional.ofNullable(readStatusMap.get(uuid));
    }

    public Optional<ReadStatus> findAllByUserId(UUID userId) {
        Map<UUID, ReadStatus> readStatusMap = loadAll();
        return readStatusMap.values().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .findFirst();
    }


    public List<UUID> findByUserIdAndChannelId(UUID channelId) {
        Map<UUID, ReadStatus> readStatusMap = loadAll();
        return readStatusMap.values().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .map(ReadStatus::getUserId)
                .collect(Collectors.toList());
    }
}
