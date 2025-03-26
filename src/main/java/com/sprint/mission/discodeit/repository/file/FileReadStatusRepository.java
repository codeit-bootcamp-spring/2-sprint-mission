package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.Empty.EmptyReadStatusListException;
import com.sprint.mission.discodeit.exception.NotFound.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.NotFound.SaveFileNotFoundException;
import com.sprint.mission.discodeit.repository.FileRepositoryImpl;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.util.CommonUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileReadStatusRepository implements ReadStatusRepository {
    private final Path path = Paths.get(System.getProperty("user.dir"), "data", "ReadStatusList.ser");

    private  Map<UUID, ReadStatus> readStatusList = new ConcurrentHashMap<>();
    private final FileRepositoryImpl<Map<UUID, ReadStatus>> fileRepository;

    public FileReadStatusRepository() {
        this.fileRepository = new FileRepositoryImpl<>(path);
        try {
            this.readStatusList = fileRepository.load();
        } catch (SaveFileNotFoundException e) {
            System.out.println("FileReadStatusRepository init");
        }
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        readStatusList.put(readStatus.getReadStatusId(), readStatus);
        fileRepository.save(readStatusList);
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> find(UUID readStatusId) {
        return Optional.ofNullable(this.readStatusList.get(readStatusId));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userID) {
        if (readStatusList.isEmpty()) {
            throw new EmptyReadStatusListException("Repository 에 저장된 읽기 상태 리스트가 없습니다.");
        }

        List<ReadStatus> list = this.readStatusList.values().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userID))
                .toList();

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

        List<ReadStatus> list = this.readStatusList.values().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .toList();

        if (list.isEmpty()) {
            throw new EmptyReadStatusListException("해당 서버에 저장된 읽기 상태 리스트가 없습니다.");
        }
        return list;
    }

    @Override
    public void delete(UUID readStatusId) {
        readStatusList.remove(readStatusId);
        fileRepository.save(readStatusList);
    }
}
