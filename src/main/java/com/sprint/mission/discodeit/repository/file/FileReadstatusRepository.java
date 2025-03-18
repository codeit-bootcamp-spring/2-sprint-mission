package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;

@Repository
public class FileReadstatusRepository extends AbstractFileRepository<ReadStatus> implements ReadStatusRepository {
    private final Map<UUID, List<ReadStatus>> userIdMap;
    private final Map<UUID, List<ReadStatus>> channelIdMap;

    public FileReadstatusRepository() {
        super(ReadStatus.class, Paths.get(System.getProperty("userStatus.dir")).resolve("src\\main\\java\\com\\sprint\\mission\\discodeit\\repository\\file\\readStatusdata"));      // 현재 프로그램이 실행되고 있는 디렉토리로 설정);
        this.userIdMap = new HashMap<>();
        this.channelIdMap = new HashMap<>();
    }

    @Override
    public void add (ReadStatus newReadStatus) {
        super.add(newReadStatus);
        this.userIdMap.computeIfAbsent(newReadStatus.getUserId(), userId -> new ArrayList<>()).add(newReadStatus);
        this.channelIdMap.computeIfAbsent(newReadStatus.getChannelId(), channelId -> new ArrayList<>()).add(newReadStatus);
    }

    @Override
    public List<ReadStatus> findByUserId(UUID userId) {
        return this.userIdMap.get(userId);
    }

    @Override
    public List<ReadStatus> findByChannelId(UUID channelId) {
        return this.channelIdMap.get(channelId);
    }

    @Override
    public void updateReadTime(UUID readStatusId, Instant readTime) {
        super.findById(readStatusId).updateReadTime(readTime);
    }

    @Override
    public void deleteById(UUID readStatusId) {
        ReadStatus target = super.findById(readStatusId);
        this.userIdMap.get(super.findById(readStatusId).getUserId()).remove(target);
        this.channelIdMap.get(super.findById(readStatusId).getChannelId()).remove(target);
        super.deleteById(readStatusId);
        super.deleteFile(readStatusId);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        this.userIdMap.remove(userId);
        List<ReadStatus> readStatusList = this.findByUserId(userId);
        readStatusList.forEach(readStatus -> channelIdMap.get(readStatus.getChannelId()).remove(readStatus));
        readStatusList.forEach(readStatus -> this.deleteById(readStatus.getId()));
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        List<ReadStatus> readStatusList = this.findByChannelId(channelId);
        this.channelIdMap.remove(channelId);
        readStatusList.forEach(readStatus -> userIdMap.get(readStatus.getUserId()).remove(readStatus));
        readStatusList.forEach(readStatus -> this.deleteById(readStatus.getId()));
    }
}
