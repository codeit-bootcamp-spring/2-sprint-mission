package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileUserService implements UserService {
    private static final String FILE_NAME = "user.ser";
    private Map<UUID, User> data;

    public FileUserService() {
        this.data = loadFromFile();
    }

    @Override
    public User create(String username, String email, String password) {
        User user = new User(username, email, password);
        data.put(user.getId(), user);
        saveToFile();
        return user;
    }

    @Override
    public User find(UUID userId) {
        User user = data.get(userId);
        return Optional.ofNullable(user)
                .orElseThrow(() -> new NoSuchElementException("User with id" + userId + "not found"));
    }

    @Override
    public List<User> findAll(){
        return new ArrayList<>(data.values());
    }

    @Override
    public User update(UUID userId, String newUsername, String newEmail, String newPassword) {
        User user = find(userId);
        user.update(newUsername, newEmail, newPassword);
        saveToFile();
        return user;
    }

    @Override
    public void delete(UUID userId) {
        if (!data.containsKey(userId)) {
            throw new NoSuchElementException("User with id" + userId + " not found");
        }
        data.remove(userId);
        saveToFile();
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(data);
        } catch (IOException e){
            throw new RuntimeException("Error saving users data", e);
        }
    }

    private Map<UUID,User> loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (Map<UUID, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error loading users data", e);
        }
    }
}