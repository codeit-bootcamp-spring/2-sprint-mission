package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.util.*;

public class FileUserRepository implements UserRepository {
    private static final FileUserRepository instance = new FileUserRepository();
    private static final String FILE_PATH = "user.ser";
    private final Map<UUID, User> data;

    private FileUserRepository() {
        this.data = loadData();
    }

    public static FileUserRepository getInstance() {
        return instance;
    }

    private void saveData() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(data);
        }
    }

    private Map<UUID, User> loadData() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    @Override
    public void save(User user) {
        data.put(user.getId(), user);
        try {
            saveData();
        } catch (IOException e) {
            throw new RuntimeException("사용자 저장 중 오류 발생", e);
        }
    }

    @Override
    public Optional<User> getUserById(UUID userId) {
        return Optional.ofNullable(data.get(userId));
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void deleteUser(UUID userId) {
        data.remove(userId);
        try {
            saveData();
        } catch (IOException e) {
            throw new RuntimeException("사용자 삭제 후 저장 중 오류 발생", e);
        }
    }
}
