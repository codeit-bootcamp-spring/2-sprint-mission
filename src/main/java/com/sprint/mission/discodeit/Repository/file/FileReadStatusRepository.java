package com.sprint.mission.discodeit.Repository.file;

import com.sprint.mission.discodeit.DTO.ReadStatus.ReadStatusCRUDDTO;
import com.sprint.mission.discodeit.Exception.CommonExceptions;
import com.sprint.mission.discodeit.Repository.FileRepositoryImpl;
import com.sprint.mission.discodeit.Repository.ReadStatusRepository;
import com.sprint.mission.discodeit.Util.CommonUtils;
import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileReadStatusRepository implements ReadStatusRepository {
    private final FileRepositoryImpl<List<ReadStatus>> fileRepository;
    private final Path path = Paths.get(System.getProperty("user.dir"), "data", "ReadStatusList.ser");

    private final List<ReadStatus> readStatusList = new ArrayList<>();

    public FileReadStatusRepository() {
        this.fileRepository = new FileRepositoryImpl<>(path);
        this.fileRepository.load();
    }

    @Override
    public void save(ReadStatus readStatus) {
        readStatusList.add(readStatus);
        fileRepository.save(readStatusList);
    }

    @Override
    public ReadStatus find(UUID readStatusId) {
        ReadStatus status = CommonUtils.findById(readStatusList, readStatusId, ReadStatus::getReadStatusId)
                .orElseThrow(() -> CommonExceptions.READ_STATUS_NOT_FOUND);
        return status;
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userID) {
        if (readStatusList.isEmpty()) {
            throw CommonExceptions.EMPTY_READ_STATUS_LIST;
        }
        List<ReadStatus> list = CommonUtils.findAllById(readStatusList, userID, ReadStatus::getUserId);

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

        List<ReadStatus> list = CommonUtils.findAllById(readStatusList, channelId, ReadStatus::getChannelId);

        if (list.isEmpty()) {
            throw CommonExceptions.EMPTY_READ_STATUS_LIST;
        }
        return list;
    }

    @Override
    public void update(ReadStatus readStatus, ReadStatusCRUDDTO readStatusCRUDDTO) {
        if (readStatusCRUDDTO.readStatusId() != null) {
            readStatus.setReadStatusId(readStatusCRUDDTO.readStatusId());
        }
        fileRepository.save(readStatusList);
    }

    @Override
    public void delete(UUID readStatusId) {
        ReadStatus status = find(readStatusId);
        readStatusList.remove(status);
        fileRepository.save(readStatusList);
    }
}
