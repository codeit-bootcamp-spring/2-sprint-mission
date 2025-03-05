package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileUserService implements UserService {
    private final String FILE_NAME = "users.dat";
    private Map<UUID, User> users = new HashMap<>();

    public FileUserService() {
        loadFromFile();
    }

    @Override
    public User create(String username, String password, String email) {
        User user = new User(UUID.randomUUID(), username, password, email);
        users.put(user.getId(), user);
        saveToFile();
        return user;
    }

    @Override
    public User find(UUID userId) {
        return users.get(userId);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User update(UUID userId, String newUsername, String newPassword, String newEmail) {
        User user = users.get(userId);
        if (user != null) {
            user.updateUsername(newUsername);
            user.setPassword(newPassword);
            user.setEmail(newEmail);
            saveToFile();
        }
        return user;
    }

    @Override
    public void delete(UUID userId) {
        users.remove(userId);
        saveToFile();
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            users = (Map<UUID, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            users = new HashMap<>();
        }
    }
}
