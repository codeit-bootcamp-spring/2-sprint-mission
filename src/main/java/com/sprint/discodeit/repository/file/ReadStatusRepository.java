package com.sprint.discodeit.repository.file;

import com.sprint.discodeit.domain.entity.ReadStatus;
import com.sprint.discodeit.domain.entity.User;
import com.sprint.discodeit.repository.util.AbstractFileRepository;
import com.sprint.discodeit.repository.util.FilePathUtil;
import com.sprint.discodeit.repository.util.SaveRepository;
import java.util.Map;
import java.util.UUID;
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
            System.out.println("[DEBUG] 동일한 UUID의 데이터가 이미 존재하므로 추가하지 않음: " + readStatus.getId());
        } else {
            readStatusMap.put(readStatus.getId(), readStatus);
            writeToFile(readStatusMap);
        }
    }

    @Override
    public void delete(UUID uuId) {

    }
}
