package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@Repository
public class FileUserRepository implements UserRepository {
    private static final String FILE_NAME = "users.ser";
    private final Map<UUID, User> data = new HashMap<>();

    public FileUserRepository() {
        loadFromFile();
    }

    @Override
    public User save(User user) {
        data.put(user.getUuid(), user);
        saveToFile();

        return data.get(user.getUuid());
    }

    @Override
    public User findByKey(UUID userKey) {
        return data.get(userKey);
    }

    @Override
    public List<User> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public boolean existsByKey(UUID userKey) {
        return data.containsKey(userKey);
    }

    @Override
    public boolean existsByName(String userName) {
        return data.values().stream().anyMatch(user -> user.getName().equals(userName));
    }

    @Override
    public boolean existsByEmail(String userEmail) {
        return data.values().stream().anyMatch(user -> user.getEmail().equals(userEmail));
    }

    @Override
    public void delete(User user) {
        data.remove(user.getUuid());
        saveToFile();
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.err.println("[Error] 사용자 데이터를 저장하는 중 문제가 발생했습니다.");
        }
    }

    private void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            Object obj = ois.readObject();
            if (obj instanceof Map<?, ?> map) {
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    if (entry.getKey() instanceof UUID key && entry.getValue() instanceof User value) {
                        data.put(key, value);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[Error] 사용자 데이터를 불러오는 중 문제가 발생했습니다.");
        }
    }
}
