package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

import java.io.*;
import java.util.*;

public class FileUserStatusRepository implements UserStatusRepository {
    private static final FileUserStatusRepository instance = new FileUserStatusRepository();
    private static final String FILE_PATH = "userStatus.ser";
    private Map<UUID, UserStatus> data;

    private FileUserStatusRepository() {
        this.data = loadData();
    }

    public static FileUserStatusRepository getInstance() {
        return instance;
    }

    private void saveData() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(data);
        }
    }

    private Map<UUID, UserStatus> loadData() {
        File file = new File(FILE_PATH);
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
    public Optional<UserStatus> getById(UUID id) {
        return Optional.ofNullable(data.get(id));
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
