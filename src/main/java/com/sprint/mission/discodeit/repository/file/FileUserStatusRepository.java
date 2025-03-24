package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.config.RepositoryProperties;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
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
public class FileUserStatusRepository implements UserStatusRepository {

    private final Path DIRECTORY;

    public FileUserStatusRepository(RepositoryProperties properties) {
        this.DIRECTORY = Paths.get(properties.getUserStatus());
        FileUtil.init(DIRECTORY);
    }

    @Override
    public UUID createUserStatus(UserStatus userStatus) {
        validateUserStatusDoesNotExist(userStatus.getUserId());

        return FileUtil.saveToFile(DIRECTORY, userStatus, userStatus.getId());
    }

    @Override
    public UserStatus findById(UUID id) {
        return (UserStatus) FileUtil.loadFromFile(DIRECTORY, id)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 UserStatus를 찾을 수 없습니다: " + id));
    }

    @Override
    public UserStatus findByUserId(UUID userId) {
        return FileUtil.loadAllFiles(DIRECTORY).stream()
                .filter(object -> object instanceof UserStatus)
                .map(object -> (UserStatus) object)
                .filter(UserStatus -> UserStatus.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 userId의 UserStatus를 찾을 수 없습니다: " + userId));
    }

    public UserStatus findByUserIdOrNull(UUID userId) {
        return FileUtil.loadAllFiles(DIRECTORY).stream()
                .filter(object -> object instanceof UserStatus)
                .map(object -> (UserStatus) object)
                .filter(UserStatus -> UserStatus.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<UserStatus> findAll() {
        return FileUtil.loadAllFiles(DIRECTORY).stream()
                .filter(object -> object instanceof UserStatus)
                .map(object -> (UserStatus) object)
                .collect(Collectors.toList());
    }

    @Override
    public void updateUserStatus(UUID id, UUID userId, Instant lastActiveAt) {
        checkUserStatusExists(id);
        UserStatus userStatus = findById(id);

        userStatus.update(lastActiveAt);
        FileUtil.saveToFile(DIRECTORY, userStatus, id);
    }

    @Override
    public void updateByUserId(UUID userId, Instant now) {
        checkUserStatusExistsByUserId(userId);
        UserStatus userStatus = findByUserId(userId);

        userStatus.updateByUserId();
        FileUtil.saveToFile(DIRECTORY, userStatus, userStatus.getId());
    }

    @Override
    public void deleteUserStatus(UUID id) {
        checkUserStatusExists(id);

        FileUtil.deleteFile(DIRECTORY, id);
    }

    /*******************************
     * Validation check
     *******************************/
    private void checkUserStatusExists(UUID id) {
        if(findById(id) == null){
            throw new NoSuchElementException("해당 ID의 UserStatus를 찾을 수 없습니다: " + id);
        }
    }

    private void checkUserStatusExistsByUserId(UUID userId) {
        if(findByUserId(userId) == null){
            throw new NoSuchElementException("해당 사용자의 UserStatus를 찾을 수 없습니다: " + userId);
        }
    }

    private void validateUserStatusDoesNotExist(UUID userId) {
        if (findByUserIdOrNull(userId) != null) {
            throw new IllegalArgumentException("이미 존재하는 객체입니다.");
        }
    }

}
