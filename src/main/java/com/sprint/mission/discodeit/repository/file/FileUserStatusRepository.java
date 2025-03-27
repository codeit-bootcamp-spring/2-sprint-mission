package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.config.RepositoryProperties;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.model.UserStatusType;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Paths;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileUserStatusRepository extends AbstractFileRepository<UserStatus> implements UserStatusRepository {
    private Map<UUID, UserStatus> userIdMap = new HashMap<>();

    public FileUserStatusRepository(RepositoryProperties repositoryProperties) {
        super(UserStatus.class, Paths.get(repositoryProperties.getFileDirectory()).resolve("UserStatusData"));      // 현재 프로그램이 실행되고 있는 디렉토리로 설정);
    }

    @Override
    public void add(UserStatus newUserStatus) {
        super.add(newUserStatus);
        super.saveToFile(directory.resolve(newUserStatus.getId().toString() + ".ser"), newUserStatus);    // file에 반영
        userIdMap.put(newUserStatus.getUserId(), newUserStatus);
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return userIdMap.containsKey(userId);
    }

    @Override
    public UserStatus findUserStatusByUserId(UUID userId) {
        if (!existsByUserId(userId)) {
            throw new NoSuchElementException("해당 userId를 가진 userStatus를 찾을 수 없습니다 : " + userId);
        }
        return userIdMap.get(userId);
    }

    @Override
    public void updateTimeById(UUID userStatusId, Instant updateTime) {
        UserStatus findUserStatus = findUserStatusByUserId(userStatusId);
        findUserStatus.updateUpdatedAt(updateTime);
        super.saveToFile(directory.resolve(findUserStatus.getId().toString() + ".ser"), findUserStatus);    // file에 반영
    }

    @Override
    public void updateTimeByUserId(UUID userId, Instant updateTime) {
        UserStatus findUserStatus = findUserStatusByUserId(userId);
        findUserStatus.updateUpdatedAt(updateTime);
        super.saveToFile(directory.resolve(findUserStatus.getId().toString() + ".ser"), findUserStatus);    // file에 반영
    }

    @Override
    public void updateUserStatusByUserId(UUID id, UserStatusType type) {
        UserStatus findUserStatus = findUserStatusByUserId(id);
        findUserStatus.setUserStatusType(type);
        super.saveToFile(directory.resolve(findUserStatus.getId().toString() + ".ser"), findUserStatus);
    }


    @Override
    public void deleteById(UUID userStatusId) {
        super.deleteById(userStatusId);
        super.deleteFile(userStatusId);
        userIdMap.remove(userStatusId);
    }
}
