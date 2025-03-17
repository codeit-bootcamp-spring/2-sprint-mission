package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class FileUserService implements UserService {
    private static final FileUserService instance = new FileUserService();
    private static final String FILE_PATH = "users.ser";
    private final Map<UUID, User> data;

    private FileUserService() {
        this.data = loadData();
    }

    public static FileUserService getInstance() {
        return instance;
    }

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
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
    public User createUser(String name) {
        User user = new User(name);
        data.put(user.getId(), user);
        saveData();
        return user;
    }

    @Override
    public Optional<User> getUserById(UUID userId) {
        return Optional.ofNullable(data.get(userId));
    }

    @Override
    public List<User> getUsersByName(String name) {
        return data.values().stream()
                .filter(user -> user.getName().equals(name))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateUser(UUID userId, String newName) {
        User user = data.get(userId);
        if (user != null) {
            Instant updatedTime = Instant.now();
            user.update(newName, updatedTime);
            saveData();
        }
    }

    @Override
    public void deleteUser(UUID userId) {
        data.remove(userId);
        saveData();
    }
}
