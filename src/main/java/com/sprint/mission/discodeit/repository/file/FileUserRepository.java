package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.util.*;

public class FileUserRepository implements UserRepository {
    private static final String FILE_NAME = "users.sar";
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
    public List<User> findAllByKeys(List<UUID> userKeys) {
        return userKeys.stream().map(data::get).toList();
    }

    @Override
    public boolean existsByKey(UUID userKey) {
        return data.containsKey(userKey);
    }

    @Override
    public void delete(User user) {
        data.remove(user.getUuid());
        saveToFile();
    }

    @Override
    public String findUserName(UUID userKey) {
        return data.get(userKey).getName();
    }

    @Override
    public String findUserId(UUID userKey) {
        return data.get(userKey).getId();
    }

    @Override
    public UUID findUserKeyById(String userId) {
        return data.values().stream()
                .filter(u -> u.getId().equals(userId))
                .map(User::getUuid)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<UUID> findUserKeyByIds(List<String> userIds) {
        return data.values().stream()
                .filter(u -> userIds.contains(u.getId()))
                .map(User::getUuid)
                .toList();
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
