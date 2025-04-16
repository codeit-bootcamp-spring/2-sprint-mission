package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.handler.UserStatusNotFoundException;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileUserStatusRepository implements UserStatusRepository {

    private final String filePath;
    private final Map<UUID, UserStatus> data;

    public FileUserStatusRepository(
        @Value("${discodeit.repository.file-directory}") String baseDir) {
        this.filePath = baseDir + "/userStatus.ser";
        this.data = loadData();
    }

    private void saveData() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(data);
        }
    }

    private Map<UUID, UserStatus> loadData() {
        File file = new File(filePath);
        if (!file.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, UserStatus>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    @Override
    public void save(UserStatus userStatus) {
        data.put(userStatus.getUserId(), userStatus);
        try {
            saveData();
        } catch (IOException e) {
            throw new RuntimeException("UserStatus 저장 중 오류 발생", e);
        }
    }

    @Override
    public UserStatus getById(UUID id) {
        UserStatus userStatus = data.get(id);
        if (userStatus == null) {
            throw new UserStatusNotFoundException(id);
        }
        return userStatus;
    }

    @Override
    public List<UserStatus> getAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
        try {
            saveData();
        } catch (IOException e) {
            throw new RuntimeException("UserStatus 삭제 중 오류 발생", e);
        }
    }
}
