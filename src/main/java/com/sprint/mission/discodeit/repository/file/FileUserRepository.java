package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.util.*;

public class FileUserRepository implements UserRepository {
    private static final String FILE_PATH = "users.ser";

    @Override
    public User save(User user) {
        Map<UUID, User> users = loadUsers();
        users.put(user.getId(), user);
        saveUsers(users);
        return user;
    }

    @Override
    public User findById(UUID userId) {
        Map<UUID, User> users = loadUsers();
        return users.get(userId);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(loadUsers().values());
    }

    @Override
    public User update(User user) {
        return save(user);
    }

    @Override
    public void delete(UUID userId) {
        Map<UUID, User> users = loadUsers();
        users.remove(userId);
        saveUsers(users);
    }

    @Override
    public boolean exists(UUID userId) {
        Map<UUID, User> users = loadUsers();
        return users.containsKey(userId);
    }

    private Map<UUID, User> loadUsers() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("users.ser not found. Generating new one.");
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("err. Returning new one.");
            return new HashMap<>();
        }
    }

    private void saveUsers(Map<UUID, User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users);
            System.out.println("users saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
