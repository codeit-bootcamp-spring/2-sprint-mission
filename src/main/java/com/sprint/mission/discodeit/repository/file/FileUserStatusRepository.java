package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FileUserStatusRepository implements UserStatusRepository {
    private final String USER_STATUS_FILE = "userStatus.ser";
    private final Map<UUID,UserStatus> userStatusData;
    private final SaveLoadHandler<UserStatus> saveLoadHandler;

    public FileUserStatusRepository() {
        saveLoadHandler = new SaveLoadHandler<>(USER_STATUS_FILE);
        userStatusData = saveLoadHandler.loadData();
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        userStatusData.put(userStatus.getId(), userStatus);
        saveLoadHandler.saveData(userStatusData);
        return userStatus;
    }

    @Override
    public UserStatus findById(UUID id) {
        return userStatusData.values().stream().
                filter(userStatus ->
                        userStatus.getUserId().equals(id)).
                findFirst().
                orElse(null);
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusData.values().stream().toList();
    }

    @Override
    public UserStatus update(UUID id) {
        UserStatus userStatusNullable = userStatusData.get(id);
        UserStatus userStatus = Optional.ofNullable(userStatusNullable).orElseThrow(() -> new NoSuchElementException(id + "가 존재하지 않습니다."));
        userStatus.updateLastLogin();
        saveLoadHandler.saveData(userStatusData);

        return userStatus;
    }

    @Override
    public void delete(UUID id) {
        if(!userStatusData.containsKey(id)){
            throw new NoSuchElementException("유저 " + id + "가 존재하지 않습니다.");
        }
        userStatusData.remove(id);
        saveLoadHandler.saveData(userStatusData);
    }
}
