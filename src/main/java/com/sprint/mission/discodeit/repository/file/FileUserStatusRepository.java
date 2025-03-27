package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class FileUserStatusRepository implements UserStatusRepository {
    private Map<UUID, UserStatus> userStatusData;
    private static final String USER_STATUS_FILE_PATH = "userStatus.ser";

    public FileUserStatusRepository() {
        dataLoad();
    }

    public void dataLoad() {
        File file = new File(USER_STATUS_FILE_PATH);
        if (!file.exists()) {
            userStatusData = new HashMap<>();
            dataSave();
            return;
        }
        try (FileInputStream fis = new FileInputStream(USER_STATUS_FILE_PATH);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            userStatusData = (Map<UUID, UserStatus>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("파일을 불러올 수 없습니다.");
        }
    }

    public void dataSave() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_STATUS_FILE_PATH))) {
            oos.writeObject(userStatusData);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("파일을 저장할 수 없습니다."+ e.getMessage(), e);
        }
    }

    public UserStatus save(UserStatus userStatus){
        this.userStatusData.put(userStatus.getId(), userStatus);
        dataSave();

        return userStatus;
    }

    public UserStatus update(UserStatusUpdateRequestDto dto){
        this.userStatusData.put(dto.getId(), userStatusData.get(dto.getId()));

        dataSave();
        return userStatusData.get(dto.getId());
    }

    public List<UserStatus> findAll(){
        return this.userStatusData.values().stream().toList();
    }

    public UserStatus findById(UUID userStatusId){
        return Optional.ofNullable(userStatusData.get(userStatusId))
                .orElseThrow(() -> new NoSuchElementException("UserStatus with id " + userStatusId + " not found"));
    }

    public UserStatus findByUserId(UUID userId){
        return userStatusData.values().stream()
                .filter(status -> status.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("UserStatus with userid " + userId + " not found"));
    }

    public void delete(UUID userStatusId){
        userStatusData.remove(userStatusId);
        dataSave();
    }
}
