package com.sprint.mission.discodeit.Repository.file;

import com.sprint.mission.discodeit.DTO.UserStatus.UserStatusCRUDDTO;
import com.sprint.mission.discodeit.Exception.NotFoundExceptions;
import com.sprint.mission.discodeit.Exception.EmptyExceptions;
import com.sprint.mission.discodeit.Repository.FileRepositoryImpl;
import com.sprint.mission.discodeit.Repository.UserStatusRepository;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileUserStatusRepository implements UserStatusRepository {
    private final FileRepositoryImpl<List<UserStatus>> fileRepository;
    private final Path path = Paths.get(System.getProperty("user.dir"), "data", "UserStatusList.ser");

    List<UserStatus> userStatusList = new ArrayList<>();

    public FileUserStatusRepository() {
        this.fileRepository = new FileRepositoryImpl<>(path);
        this.fileRepository.load();
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        userStatusList.add(userStatus);
        fileRepository.save(userStatusList);
        return userStatus;
    }

    @Override
    public UserStatus find(UUID userId) {
        UserStatus status = userStatusList.stream().filter(userStatus -> userStatus.getUserId().equals(userId))
                .findFirst().orElseThrow(() -> NotFoundExceptions.USER_STATUS_NOT_FOUND);
        return status;
    }

    @Override
    public List<UserStatus> findAll() {
        if (userStatusList.isEmpty()) {
            throw EmptyExceptions.EMPTY_USER_STATUS_LIST;
        }
        return userStatusList;
    }

    @Override
    public UserStatus update(UserStatus userStatus, UserStatusCRUDDTO userStatusUpdateDTO) {
        if (userStatusUpdateDTO.userStatusId() != null) {
            userStatus.setUserStatusId(userStatusUpdateDTO.userStatusId());
        }
        fileRepository.save(userStatusList);
        return userStatus;
    }

    @Override
    public void delete(UUID userId) {
        UserStatus userStatus = find(userId);
        userStatusList.remove(userStatus);
        fileRepository.save(userStatusList);
    }
}
