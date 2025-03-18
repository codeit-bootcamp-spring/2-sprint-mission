package com.sprint.mission.discodeit.Repository.file;

import com.sprint.mission.discodeit.DTO.legacy.UserStatus.UserStatusCRUDDTO;
import com.sprint.mission.discodeit.Exception.Empty.EmptyUserStatusListException;
import com.sprint.mission.discodeit.Exception.NotFound.SaveFileNotFoundException;
import com.sprint.mission.discodeit.Exception.NotFound.UserStatusNotFoundException;
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

        try {
            this.userStatusList = fileRepository.load();
        } catch (SaveFileNotFoundException e) {
            System.out.println("FileUserStatusRepository init");
        }
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        userStatusList.add(userStatus);
        fileRepository.save(userStatusList);
        return userStatus;
    }

    @Override
    public UserStatus findByUserId(UUID userId) {
        UserStatus status = userStatusList.stream().filter(userStatus -> userStatus.getUserId().equals(userId))
                .findFirst().orElseThrow(() -> new UserStatusNotFoundException("유저 상태를 찾지 못했습니다."));
        return status;
    }

    @Override
    public UserStatus findByStatusId(UUID userStatusId) {
        UserStatus status = userStatusList.stream().filter(userStatus -> userStatus.getUserStatusId().equals(userStatusId))
                .findFirst().orElseThrow(() -> new UserStatusNotFoundException("유저 상태를 찾지 못했습니다."));
        return status;
    }

    @Override
    public List<UserStatus> findAll() {
        if (userStatusList.isEmpty()) {
            throw new EmptyUserStatusListException("Repository 내 유저 상태 리스트가 비어있습니다.");
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
    public void delete(UUID id) {
        try {
            UserStatus userStatus = findByUserId(id);
            userStatusList.remove(userStatus);
            fileRepository.save(userStatusList);
        } catch (UserStatusNotFoundException e) {
            UserStatus userStatus = findByStatusId(id);
            userStatusList.remove(userStatus);
            fileRepository.save(userStatusList);
        }
    }
}
