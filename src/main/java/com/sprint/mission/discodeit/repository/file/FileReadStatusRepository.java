package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.config.RepositoryProperties;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.util.FileUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file", matchIfMissing = true)
public class FileReadStatusRepository implements ReadStatusRepository {

    private final Path DIRECTORY;

    public FileReadStatusRepository(RepositoryProperties properties) {
        this.DIRECTORY = Paths.get(properties.getReadStatus());
        FileUtil.init(DIRECTORY);
    }

    @Override
    public UUID createReadStatus(ReadStatus readStatus) {
        validateReadStatusDoesNotExist(readStatus.getUserId(), readStatus.getChannelId());

        return FileUtil.saveToFile(DIRECTORY, readStatus, readStatus.getId());
    }

    @Override
    public ReadStatus findById(UUID id) {
        return (ReadStatus) FileUtil.loadFromFile(DIRECTORY, id)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 ReadStatus를 찾을 수 없습니다: " + id));
    }

    @Override
    public ReadStatus findByUserIdAndChannelId(UUID userId, UUID channelId) {
        return FileUtil.loadAllFiles(DIRECTORY).stream()
                .filter(object -> object instanceof ReadStatus)
                .map(object -> (ReadStatus) object)
                .filter(ReadStatus -> ReadStatus.getUserId().equals(userId) && ReadStatus.getChannelId().equals(channelId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 사용자 및 채널을 가진 ReadStatus를 찾을 수 없습니다: " + userId + "/" + channelId));
    }

    public ReadStatus findByUserIdAndChannelIdOrNull(UUID userId, UUID channelId) {
        return FileUtil.loadAllFiles(DIRECTORY).stream()
                .filter(object -> object instanceof ReadStatus)
                .map(object -> (ReadStatus) object)
                .filter(ReadStatus -> ReadStatus.getUserId().equals(userId) && ReadStatus.getChannelId().equals(channelId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return FileUtil.loadAllFiles(DIRECTORY).stream()
                .filter(object -> object instanceof ReadStatus)
                .map(object -> (ReadStatus) object)
                .filter(ReadStatus -> ReadStatus.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public void updateReadStatus(UUID id, Instant lastReadAt) {
        checkReadStatusExists(id);
        ReadStatus readStatus = findById(id);

        readStatus.update(lastReadAt);
        FileUtil.saveToFile(DIRECTORY, readStatus, id);
    }

    @Override
    public void deleteReadStatus(UUID id) {
        checkReadStatusExists(id);
        FileUtil.deleteFile(DIRECTORY, id);
    }

    @Override
    public void deleteReadStatusByChannelId(UUID channelId) {
        ReadStatus readStatus = FileUtil.loadAllFiles(DIRECTORY).stream()
                .filter(object -> object instanceof ReadStatus)
                .map(object -> (ReadStatus) object)
                .filter(readStatusObj -> readStatusObj.getChannelId().equals(channelId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 채널을 가진 ReadStatus를 찾을 수 없습니다: " + channelId));

        FileUtil.deleteFile(DIRECTORY, readStatus.getId());
    }

    /*******************************
     * Validation check
     *******************************/
    private void checkReadStatusExists(UUID id) {
        if(findById(id) == null){
            throw new NoSuchElementException("해당 ID의 ReadStatus를 찾을 수 없습니다: " + id);
        }
    }

    private void validateReadStatusDoesNotExist(UUID userId, UUID channelId) {
        if (findByUserIdAndChannelIdOrNull(userId, channelId) != null) {
            throw new IllegalArgumentException("이미 존재하는 객체입니다.");
        }
    }

}
