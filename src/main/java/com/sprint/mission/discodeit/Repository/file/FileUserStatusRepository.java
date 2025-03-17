package com.sprint.mission.discodeit.Repository.file;

import com.sprint.mission.discodeit.DTO.UserStatus.UserStatusCRUDDTO;
import com.sprint.mission.discodeit.Exception.CommonExceptions;
import com.sprint.mission.discodeit.Repository.FileRepositoryImpl;
import com.sprint.mission.discodeit.Repository.UserStatusRepository;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileUserStatusRepository implements UserStatusRepository {
    private final FileRepositoryImpl<List<UserStatus>> fileRepository;
    private final Path path = Paths.get(System.getProperty("user.dir"), "data", "UserStatusList.ser");

    List<UserStatus> userStatusList = new ArrayList<>();

    public FileUserStatusRepository() {
        this.fileRepository = new FileRepositoryImpl<>(path);
        this.fileRepository.load();
    }

    @Override
    public void save(UserStatus userStatus) {
        userStatusList.add(userStatus);
        fileRepository.save(userStatusList);

    }

    @Override
    public UserStatus find(UUID userId) {
        UserStatus status = userStatusList.stream().filter(userStatus -> userStatus.getUserId().equals(userId))
                .findFirst().orElseThrow(() -> CommonExceptions.USER_STATUS_NOT_FOUND);
        return status;
    }

    @Override
    public List<UserStatus> findAll() {
        if (userStatusList.isEmpty()) {
            throw CommonExceptions.EMPTY_USER_STATUS_LIST;
        }
        return userStatusList;
    }

    @Override
    public void update(UserStatus userStatus, UserStatusCRUDDTO userStatusUpdateDTO) {
        if (userStatusUpdateDTO.userStatusId() != null) {
            userStatus.setUserStatusId(userStatusUpdateDTO.userStatusId());
        }
        fileRepository.save(userStatusList);
    }

    @Override
    public void delete(UUID userId) {
        UserStatus userStatus = find(userId);
        userStatusList.remove(userStatus);
        fileRepository.save(userStatusList);
    }
}
