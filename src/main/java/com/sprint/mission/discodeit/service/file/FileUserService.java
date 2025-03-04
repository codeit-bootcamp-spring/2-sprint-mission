package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileUserService implements UserService {
    private static final String FILE_PATH = "users.ser";

    @Override
    public User create(String username, String email, String password) {
        User user = new User(username, email, password);
        Map<UUID, User> users = loadUsers();
        users.put(user.getId(), user);
        saveUsers(users);
        return user;
    }

    @Override
    public User find(UUID userId) {
        Map<UUID, User> users = loadUsers();
        User user = users.get(userId);
        if (user == null) {
            throw new NoSuchElementException("User with id " + userId + " not found");
        }
        return user;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(loadUsers().values());
    }

    @Override
    public User update(UUID userId, String newUsername, String newEmail, String newPassword) {
        Map<UUID, User> users = loadUsers();
        User user = users.get(userId);
        if (user == null) {
            throw new NoSuchElementException("User with id " + userId + " not found");
        }
        user.update(newUsername, newEmail, newPassword);
        saveUsers(users);
        return user;
    }

    @Override
    public void delete(UUID userId) {
        Map<UUID, User> users = loadUsers();
        if (!users.containsKey(userId)) {
            throw new NoSuchElementException("User with id " + userId + " not found");
        }
        users.remove(userId);
        saveUsers(users);
    }

    private Map<UUID, User> loadUsers() {
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

    private void saveUsers(Map<UUID, User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
