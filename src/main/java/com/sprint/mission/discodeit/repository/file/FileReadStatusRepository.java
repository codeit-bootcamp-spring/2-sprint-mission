package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.DTO.legacy.ReadStatus.ReadStatusCRUDDTO;
import com.sprint.mission.discodeit.exception.Empty.EmptyReadStatusListException;
import com.sprint.mission.discodeit.exception.NotFound.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.NotFound.SaveFileNotFoundException;
import com.sprint.mission.discodeit.repository.FileRepositoryImpl;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.util.CommonUtils;
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

    private List<ReadStatus> readStatusList = new ArrayList<>();

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
        readStatusList.add(readStatus);
        fileRepository.save(readStatusList);
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
        fileRepository.save(readStatusList);
        return readStatus;
    }

    @Override
    public void delete(UUID readStatusId) {
        ReadStatus status = find(readStatusId);
        readStatusList.remove(status);
        fileRepository.save(readStatusList);
    }
}
