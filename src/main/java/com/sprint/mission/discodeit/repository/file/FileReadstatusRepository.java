package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class FileReadstatusRepository extends AbstractFileRepository<ReadStatus> implements ReadStatusRepository {
    private final Map<UUID, UUID> userIdMap;
    private final Map<UUID, UUID> channelIdMap;

    public FileReadstatusRepository() {
        super(ReadStatus.class, Paths.get(System.getProperty("userStatus.dir")).resolve("src\\main\\java\\com\\sprint\\mission\\discodeit\\repository\\file\\readStatusdata"));      // 현재 프로그램이 실행되고 있는 디렉토리로 설정);
        this.userIdMap = new HashMap<>();
        this.channelIdMap = new HashMap<>();
    }

    @Override
    public void add (ReadStatus readStatus) {
        super.add(readStatus);
        this.userIdMap.put(readStatus.getUserId(), readStatus.getId());
        this.channelIdMap.put(readStatus.getChannelId(), readStatus.getId());
    }

    @Override
    public ReadStatus findByUserId(UUID userId) {
        return super.findById(userIdMap.get(userId));
    }

    @Override
    public ReadStatus findByChannelId(UUID channelId) {
        return super.findById(channelIdMap.get(channelId));
    }

    @Override
    public void deleteById(UUID readStatusId) {
        super.deleteById(readStatusId);
        super.deleteFile(readStatusId);
        this.userIdMap.remove(readStatusId);
        this.channelIdMap.remove(readStatusId);
    }
}
