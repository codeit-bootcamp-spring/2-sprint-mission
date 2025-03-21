package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileUserStatusRepository implements UserStatusRepository {
    private final Map<UUID, UserStatus> data = new HashMap<>();
    private static final String FILE_NAME = "userStatus.ser";
    private final String filePath;

    public FileUserStatusRepository(String directory) {
        this.filePath = directory + "/" + FILE_NAME;
        loadFromFile();
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        data.put(userStatus.getUserKey(), userStatus);
        saveToFile();

        return userStatus;
    }

    @Override
    public void delete(UUID userStatusKey) {
        data.remove(userStatusKey);
        saveToFile();
    }

    @Override
    public List<UserStatus> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public boolean existsByUserKey(UUID userKey) {
        return data.values().stream().anyMatch(userStatus -> userStatus.getUserKey().equals(userKey));
    }

    @Override
    public UserStatus findByUserKey(UUID userKey) {
        return data.values().stream()
                .filter(userStatus -> userStatus.getUserKey().equals(userKey))
                .findFirst()
                .orElse(null);
    }

    @Override
    public UserStatus findByKey(UUID userStatusKey) {
        return data.get(userStatusKey);
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.err.println("[Error] 사용자 데이터를 저장하는 중 문제가 발생했습니다.");
        }
    }

    private void loadFromFile() {
        File file = new File(filePath);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof Map<?, ?> map) {
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    if (entry.getKey() instanceof UUID key && entry.getValue() instanceof UserStatus value) {
                        data.put(key, value);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[Error] 사용자 데이터를 불러오는 중 문제가 발생했습니다.");
        }
    }
}
